package com.zb.agent.impl;

import com.zb.agent.AgentService;
import com.zb.agent.event.MsgEventHandle;
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

import java.util.List;


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
    private ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

    @Override
    public void startTask(String query, ChatModel chatModel, ToolCallback[] callbacks, MsgEventHandle eventHandle) {
        // 手动管理 function call的执行
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(callbacks)
                .internalToolExecutionEnabled(false)
                .build();
        // 组装提示词
        Prompt prompt = new Prompt(
                List.of(new SystemMessage("You are a helpful assistant."), new UserMessage(query)),
                chatOptions);
        run(prompt, chatModel, eventHandle);
    }
    private void run(Prompt prompt, ChatModel chatModel, MsgEventHandle eventHandle){
//        chatModel.stream(prompt)
//                .doOnNext(chatResponseSignal -> {
//                    if (chatResponseSignal.hasToolCalls()) {
//                    }
//                })
    }
}
