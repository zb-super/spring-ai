package com.bz.agent.executor;

import com.bz.agent.model.chat.ModelResponse;
import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.model.agent.AgentContext;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;


public abstract class AbstractAgentExecutor implements AgentExecutor {

    @Override
    public Flux<AgentChatResponse> chatStream(AgentContext agentContext) {
        return Flux.create(fluxSink -> {
            ExecutorContext executorContext = buildExecutorContext(agentContext, fluxSink);
            doChatSteam(executorContext, fluxSink);
        });
    }

    /**
     * 执行逻辑
     * @param context
     * @param emitter
     */
    protected void doChatSteam(ExecutorContext context, FluxSink<AgentChatResponse> emitter){
        context.getChatModel()
                .stream(context.getPrompt())
                .doOnNext(chatResponse -> AgentExecutorUtils.sendThinkMsg(context.getIndex(), chatResponse, emitter))
                .collectList()
                .subscribe(chatResponseList -> {
                    //处理模型返回的结果
                    ModelResponse modelResponse = AgentExecutorUtils.processChatResponse(chatResponseList);
                    // 1.是否需要执行工具

                    // 2.是否需要查询知识库

                    // 3.是否需要调用工作流

                    // 4.直接返回给模型

                    // 1.是否需要执行工具
//                    if (modelResponse.isNeedExecTool()) {
//                        processFunctionCall(modelResponse.getToolList(), context, emitter);
//                    }else {
//
//                        //4.直接返回给模型
//                        emitter.next(AgentChatResponse.ofTextResponse(modelResponse.toString(), context.getIndex()));
//                        // 停止符号
//                        emitter.next(AgentChatResponse.ofStopResponse(context.getIndex()));
//                    }
                });
    }

    /**
     * 构建模型
     * @param context 上下文
     * @param callingManager 执行器
     * @return
     */
    protected abstract ChatModel buildChatModel(AgentContext context, ToolCallingManager callingManager);

    /**
     * 构建提示词
     * @param context 上下文
     * @param chatOptions
     * @return
     */
    protected abstract Prompt buildPrompt(AgentContext context, ChatOptions chatOptions);

    /**
     * 构建聊天会话选项
     * @param context 上下文
     * @return
     */
    protected abstract ChatOptions buildChatOptions(AgentContext context);

    /**
     * 构建执行器
     * @param agentContext 上下文
     * @param emitter 发射器
     * @return
     */
    protected abstract ToolCallingManager buildToolCallingManager(AgentContext agentContext, FluxSink<AgentChatResponse> emitter);

    /**
     * 构建执行上下文
     * @param agentContext 山下文
     * @param emitter 发射器
     * @return
     */
    private ExecutorContext buildExecutorContext(AgentContext agentContext, FluxSink<AgentChatResponse> emitter) {
        // 模型信息
        ChatOptions chatOptions = buildChatOptions(agentContext);
        // 构建提示词
        Prompt prompt = buildPrompt(agentContext, chatOptions);
        // 工具构建执行器
        ToolCallingManager toolCallingManager = buildToolCallingManager(agentContext, emitter);
        // 构建知识库执行器

        // 构建模型
        ChatModel chatModel = buildChatModel(agentContext, toolCallingManager);
        return ExecutorContext.builder()
                .prompt(prompt)
                .chatModel(chatModel)
                .toolCallingManager(toolCallingManager)
                .build();
    }
}
