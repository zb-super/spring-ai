package com.bz.agent.executor;

import com.bz.agent.model.agent.AgentContext;
import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.tool.AgentToolCallingManager;
import com.bz.agent.tool.DefaultAgentToolCallingManager;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.openai.inference.OpenAiInferenceChatModel;
import org.springframework.ai.openai.inference.api.OpenAiInferenceApi;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;

public class QwenAgentExecutor extends AbstractAgentExecutor implements AgentExecutor {

//    private final AgentToolCallingManager toolCallingManager = DefaultAgentToolCallingManager.builder().build();

    @Override
    protected void doChatSteam(ExecutorContext context, FluxSink<AgentChatResponse> emitter){
        doChatWithStream(context, emitter);
    }

    private void doChatWithStream(ExecutorContext context, FluxSink<AgentChatResponse> emitter){
        context.getChatModel().stream(context.getPrompt())
                .doOnNext(chatResponse -> sendThinkMsg(context.getIndex(), chatResponse, emitter))
                .collectList()
                .subscribe(chatResponseList -> {
                    List<ChatResponse> toolList = new ArrayList<>();
                    StringBuilder thinkBuilder = new StringBuilder();
                    StringBuilder textBuilder = new StringBuilder();
                    processChatResponse(toolList, thinkBuilder, textBuilder, chatResponseList);
                    if (!toolList.isEmpty()) {
                        processFunctionCall(toolList, context, emitter);
                    }else {
                        // 大模型总结完
                        emitter.next(AgentChatResponse.ofTextResponse(textBuilder.toString(), context.getIndex()));
                        // 停止符号
                        emitter.next(AgentChatResponse.ofStopResponse(context.getIndex()));
                    }
                });
    }

    private void processFunctionCall(List<ChatResponse> toolList,
                                     ExecutorContext context,
                                     FluxSink<AgentChatResponse> emitter){
        toolList.forEach(chatResponse -> {
            // 执行function call
            var toolExecutionResult = super.callingManager.executeToolCalls(context.getPrompt(), chatResponse);

            // 直接返回给前端
            if (toolExecutionResult.returnDirect()) {
                List<Generation> generations = ToolExecutionResult.buildGenerations(toolExecutionResult);
                for (Generation generation : generations) {
                    // 强制改变
                    String text = generation.getOutput().getText();
                    emitter.next(AgentChatResponse.ofToolResultChatResponse(text, context.getIndex()));
                    emitter.next(AgentChatResponse.ofStopResponse(context.getIndex()));
                }
                // 停止符号
                emitter.next(AgentChatResponse.ofStopResponse(context.getIndex()));
            }else {

                doChatWithStream(context.addIndex(), emitter);
            }
        });
    }

    @Override
    protected ChatModel buildChatModel(AgentContext context, ToolCallingManager callingManager){
        OpenAiInferenceApi openAiApi = OpenAiInferenceApi.builder()
                .apiKey(context.getChatOptions().getApiKey())
                .baseUrl(context.getChatOptions().getBaseUrl())
                .build();
        return OpenAiInferenceChatModel.builder()
                .openAiApi(openAiApi)
                .toolCallingManager(callingManager)
                .retryTemplate(RetryTemplate.defaultInstance())
                .observationRegistry(ObservationRegistry.create())
                .toolExecutionEligibilityPredicate(new DefaultToolExecutionEligibilityPredicate())
                .build();
    }
}
