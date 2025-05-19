package com.bz.agent.executor;

import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.model.agent.AgentContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.inference.metadata.MsgMetadataType;
import org.springframework.ai.tool.execution.ToolExecutionException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.Map;

public abstract class AbstractAgentExecutor implements AgentExecutor {

    protected ToolCallingManager toolCallingManager;

    @Override
    public Flux<AgentChatResponse> chatStream(AgentContext agentContext) {
        return Flux.create(fluxSink -> {
            // 模型信息
            ChatOptions chatOptions = buildChatOptions(agentContext);
            // 构建提示词
            Prompt prompt = buildPrompt(agentContext, chatOptions);
            // 构建执行器
            toolCallingManager = buildToolCallingManager(agentContext, fluxSink);
            // 构建模型
            ChatModel chatModel = buildChatModel(agentContext, toolCallingManager);

            ExecutorContext context = ExecutorContext.builder()
                    .prompt(prompt)
                    .chatModel(chatModel)
                    .build();

            doChatSteam(context, fluxSink);
        });
    }

    /**
     *  构建模型
     * @param context
     * @return
     */
    protected abstract ChatModel buildChatModel(AgentContext context, ToolCallingManager callingManager);

    /**
     * 逻辑
     * @param context
     * @param emitter
     */
    protected abstract void doChatSteam(ExecutorContext context, FluxSink<AgentChatResponse> emitter);


    /**
     * 构建执行器
     * @param agentContext
     * @return
     */
    protected abstract ToolCallingManager buildToolCallingManager(AgentContext agentContext, FluxSink<AgentChatResponse> emitter);

    /**
     * 构建聊天信息
     * @param context
     * @return
     */
    protected ChatOptions buildChatOptions(AgentContext context){
        return ToolCallingChatOptions.builder()
                .toolCallbacks(context.getCallbacks())
                .internalToolExecutionEnabled(false)
                .temperature(context.getChatOptions().getTemperature())
                .model(context.getChatOptions().getModel())
                .maxTokens(context.getChatOptions().getMaxToken())
                .toolContext(context.getToolContext())
                .build();
    }

    /**
     * 构建提示词
     * @param context
     * @param chatOptions
     * @return
     */
    protected Prompt buildPrompt(AgentContext context, ChatOptions chatOptions){
        String userInput = context.getUser().getUserInput();
        // 最新内容消息
        List<AbstractMessage> abstractMessages = List.of(new SystemMessage(context.getPrompt()), new UserMessage(userInput));
        // 历史消息
        List<Message> historyMessages = context.getMessages();
        historyMessages.addAll(abstractMessages);
        return new Prompt(context.getMessages(), chatOptions);
    }

    /**
     * 判断聊天内容是否为空
     * @param chatResponse
     * @return
     */
    protected boolean isBlank(ChatResponse chatResponse){
        String text = chatResponse.getResult().getOutput().getText();
        return StringUtils.isBlank(text);
    }

    /**
     * 类型匹配
     * @param chatResponse
     * @param target
     * @return
     */
    protected boolean isTypeContext(ChatResponse chatResponse, MsgMetadataType target){
        Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
        Object type = metadata.get("type");
        return type.equals(target);
    }

    /**
     * 获取内容
     * @param chatResponse
     * @return
     */
    protected String getContentText(ChatResponse chatResponse){
        return chatResponse.getResult().getOutput().getText();
    }

    /**
     * 发送思考内容
     * @param index
     * @param chatResponse
     * @param emitter
     */
    protected void sendThinkMsg(int index, ChatResponse chatResponse, FluxSink<AgentChatResponse> emitter){
        String text = chatResponse.getResult().getOutput().getText();
        if (isTypeContext(chatResponse, MsgMetadataType.THINK) && StringUtils.isNotBlank(text)) {
            emitter.next(AgentChatResponse.ofThinkResponse(text, index));
        }
    }

    /**
     * 处理聊天内容
     * @param toolList
     * @param thinkBuilder
     * @param textBuilder
     * @param chatResponseList
     */
    protected void processChatResponse(List<ChatResponse> toolList, StringBuilder thinkBuilder, StringBuilder textBuilder,
                                     List<ChatResponse> chatResponseList){
        for (ChatResponse chatResponse : chatResponseList) {
            if (chatResponse.hasToolCalls()){
                toolList.add(chatResponse);
                continue;
            }
            if (isBlank(chatResponse)){
                continue;
            }
            if (isTypeContext(chatResponse, MsgMetadataType.THINK)){
                thinkBuilder.append(getContentText(chatResponse));
            }else if (isTypeContext(chatResponse, MsgMetadataType.TEXT)){
                textBuilder.append(getContentText(chatResponse));
            }
        }
    }

    protected void sendFunctionCallName(int index, String toolName, FluxSink<AgentChatResponse> emitter){
        emitter.next(AgentChatResponse.ofToolBeforeResponse(toolName,index));
    }

    protected void sendFunctionError(int index, String toolName, ToolExecutionException ex, FluxSink<AgentChatResponse> emitter){
        emitter.next(AgentChatResponse.ofErrorResponse("调用工具出错" + toolName +"错误信息：" + ex.getMessage() +"。", index));
    }
}
