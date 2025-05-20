package com.bz.agent.executor;

import com.bz.agent.model.agent.AgentContext;
import com.bz.agent.model.chat.ModelResponse;
import com.bz.agent.model.response.AgentChatResponse;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.openai.inference.OpenAiInferenceChatModel;
import org.springframework.ai.openai.inference.api.OpenAiInferenceApi;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.observation.ToolCallingObservationContext;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.FluxSink;

import java.util.List;

public class QwenAgentExecutor extends AbstractAgentExecutor implements AgentExecutor {

    @Override
    protected void doChatSteam(ExecutorContext context, FluxSink<AgentChatResponse> emitter){
        context.getChatModel()
                .stream(context.getPrompt())
                .doOnNext(chatResponse -> sendThinkMsg(context.getIndex(), chatResponse, emitter))
                .collectList()
                .subscribe(chatResponseList -> {
                    //处理模型返回的结果
                    ModelResponse modelResponse = processChatResponse(chatResponseList);
                    // 1.是否需要执行工具

                    // 2.是否需要查询知识库

                    // 3.是否需要调用工作流

                    // 4.直接返回给模型



                    // 1.是否需要执行工具
                    if (modelResponse.isNeedExecTool()) {
                        processFunctionCall(modelResponse.getToolList(), context, emitter);
                    }else {

                        //4.直接返回给模型
                        emitter.next(AgentChatResponse.ofTextResponse(modelResponse.toString(), context.getIndex()));
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
            var toolExecutionResult = context.getToolCallingManager().executeToolCalls(context.getPrompt(), chatResponse);
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
                doChatSteam(context.addIndex()
                        .setPrompt(toolExecutionResult.conversationHistory()), emitter);
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

    @Override
    protected ToolCallingManager buildToolCallingManager(AgentContext agentContext, FluxSink<AgentChatResponse> emitter) {
        ObservationRegistry registry = ObservationRegistry.create();
        //
        registry.observationConfig().observationHandler(new ObservationHandler<>() {

            @Override
            public boolean supportsContext(Observation.Context context) {
                return context instanceof ToolCallingObservationContext;
            }

            @Override
            public void onStart(Observation.Context context) {
                System.out.println("onStart");
            }

            @Override
            public void onError(Observation.Context context) {
                System.out.println("onError");
            }

            @Override
            public void onStop(Observation.Context context) {
                System.out.println("onStop");
            }
        });

        DefaultToolCallingManager callingManager = DefaultToolCallingManager.builder()
                .observationRegistry(registry)
                .toolExecutionExceptionProcessor(new DefaultToolExecutionExceptionProcessor(false))
                .build();

        return callingManager;
    }
}
