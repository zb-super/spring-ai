package com.bz.common.agent.impl;

import com.bz.common.agent.AgentService;
import com.bz.common.agent.model.AgentMsg;
import com.bz.common.agent.model.AgentMsgType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.Objects;


/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/12
 */
@Service
public class SpringAgentService implements AgentService {
    /**
     * 执行器
     */
    private final ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

    @Override
    public Flux<AgentMsg> startTask(String query, ChatModel chatModel, ToolCallback[] callbacks) {
        // 手动管理 function call的执行
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(callbacks)
                .internalToolExecutionEnabled(false)
                .build();

        // 组装提示词
        Prompt prompt = new Prompt(
                List.of(new SystemMessage("You are a helpful assistant."), new UserMessage(query)),
                chatOptions);

        return Flux.create(fluxSink -> run(prompt, chatModel, fluxSink));
    }

    private void run(Prompt prompt, ChatModel chatModel, FluxSink<AgentMsg> emitter){
        Objects.requireNonNull(chatModel.stream(prompt)
                        .doOnNext(item -> {
                            String text = item.getResult().getOutput().getText();
                            emitter.next(new AgentMsg(text, AgentMsgType.TEXT));
                        })
                        .collectList()
                        .block())
                .stream()
                .filter(ChatResponse::hasToolCalls)
                .map(chatResponse ->
                        toolCallingManager.executeToolCalls(prompt, chatResponse).conversationHistory())
                .forEach(messages -> run(new Prompt(messages, prompt.getOptions()), chatModel, emitter));
    }
}
