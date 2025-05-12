package com.zb.impl;

import com.zb.AgentService;
import com.zb.event.MsgEventHandle;
import com.zb.utils.TestUtils;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.stereotype.Service;

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

    private OpenAiChatModel chatModel;

    /**
     * 执行器
     */
    private ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

    @Override
    public void startTask(Long botId, String query, MsgEventHandle msgEventHandle) {

        // 根据botId 找出所有的配置来
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(ToolCallbacks.from(new TestUtils()))
                .internalToolExecutionEnabled(false)
                .build();

        Prompt prompt = new Prompt(
                List.of(new SystemMessage("You are a helpful assistant."), new UserMessage("今天天气怎么样")),
                chatOptions);
    }
}
