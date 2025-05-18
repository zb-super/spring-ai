package com.bz.agent.executor;

import com.bz.agent.model.agent.AgentContext;
import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.tool.AgentToolCallingManager;
import com.bz.agent.tool.DefaultAgentToolCallingManager;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.openai.inference.OpenAiInferenceChatModel;
import org.springframework.ai.openai.inference.api.OpenAiInferenceApi;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;

@Service
public class QwenAgentExecutor extends AbstractAgentExecutor implements AgentExecutor {

    private final AgentToolCallingManager toolCallingManager = DefaultAgentToolCallingManager.builder().build();

    @Override
    protected void doChatSteam(Prompt prompt, ChatModel chatModel, FluxSink<AgentChatResponse> emitter){
        doChatWithStream(0, prompt, chatModel, emitter);
    }

    private void doChatWithStream(int index, Prompt prompt, ChatModel chatModel, FluxSink<AgentChatResponse> emitter){
        chatModel.stream(prompt)
                .doOnNext(chatResponse -> sendThinkMsg(index, chatResponse, emitter))
                .collectList()
                .subscribe(chatResponseList -> {
                    List<ChatResponse> toolList = new ArrayList<>();
                    StringBuilder thinkBuilder = new StringBuilder();
                    StringBuilder textBuilder = new StringBuilder();
                    processChatResponse(toolList, thinkBuilder, textBuilder, chatResponseList);
                    if (!toolList.isEmpty()) {
                        processFunctionCall(toolList, index, prompt, chatModel, emitter);
                    }else {
                        // 大模型总结完
                        emitter.next(AgentChatResponse.ofTextResponse(textBuilder.toString(), index));
                        // 停止符号
                        emitter.next(AgentChatResponse.ofStopResponse(index));
                    }
                });
    }

    private void processFunctionCall(List<ChatResponse> toolList,
                                     int index,
                                     Prompt prompt,
                                     ChatModel chatModel,
                                     FluxSink<AgentChatResponse> emitter){
        toolList.forEach(chatResponse -> {
            // 执行function call
            var toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse,
                    toolName -> sendFunctionCallName(index, toolName, emitter),
                    (toolName,result) -> {},
                    (toolName,ex) -> sendFunctionError(index, toolName, ex, emitter));

            // 直接返回给前端
            if (toolExecutionResult.returnDirect()) {
                List<Generation> generations = ToolExecutionResult.buildGenerations(toolExecutionResult);
                for (Generation generation : generations) {
                    String text = generation.getOutput().getText();
                    emitter.next(AgentChatResponse.ofToolResultChatResponse(text, index));
                    emitter.next(AgentChatResponse.ofStopResponse(index));
                }
                // 停止符号
                emitter.next(AgentChatResponse.ofStopResponse(index));
            }else {
                doChatWithStream(index + 1 ,new Prompt(toolExecutionResult.conversationHistory(), prompt.getOptions()), chatModel, emitter);
            }
        });
    }

    @Override
    protected ChatModel buildChatModel(AgentContext context){
        OpenAiInferenceApi openAiApi = OpenAiInferenceApi.builder()
                .apiKey(context.getChatOptions().getApiKey())
                .baseUrl(context.getChatOptions().getBaseUrl())
                .build();
        return OpenAiInferenceChatModel.builder()
                .openAiApi(openAiApi)
                .toolCallingManager(toolCallingManager)
                .retryTemplate(RetryTemplate.defaultInstance())
                .observationRegistry(ObservationRegistry.create())
                .toolExecutionEligibilityPredicate(new DefaultToolExecutionEligibilityPredicate())
                .build();
    }
}
