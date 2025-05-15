package com.bz.agent.executor;

import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.model.chat.ChatContext;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;

public abstract class AbstractAgentExecutor implements AgentExecutor {

    @Override
    public Flux<AgentChatResponse> chatStream(ChatContext context) {
        ChatOptions chatOptions = buildChatOptions(context);
        Prompt prompt = buildPrompt(context, chatOptions);
        ChatModel chatModel = buildChatModel(context);
        return Flux.create(fluxSink -> doChatSteam(prompt, chatModel, fluxSink));
    }

    protected abstract ChatModel buildChatModel(ChatContext context);

    protected abstract void doChatSteam(Prompt prompt, ChatModel chatModel, FluxSink<AgentChatResponse> emitter);

    protected ChatOptions buildChatOptions(ChatContext context){
        return ToolCallingChatOptions.builder()
                .toolCallbacks(context.getCallbacks())
                .internalToolExecutionEnabled(false)
                .temperature(context.getChatOptions().getTemperature())
                .model(context.getChatOptions().getModel())
                .maxTokens(context.getChatOptions().getMaxToken())
                .build();
    }

    protected Prompt buildPrompt(ChatContext context, ChatOptions chatOptions){
        String userInput = context.getUser().getUserInput();
        // 最新内容消息
        List<AbstractMessage> abstractMessages = List.of(new SystemMessage(context.getPrompt()), new UserMessage(userInput));
        // 历史消息
        List<Message> historyMessages = context.getMessages();
        historyMessages.addAll(abstractMessages);
        return new Prompt(context.getMessages(), chatOptions);
    }
}
