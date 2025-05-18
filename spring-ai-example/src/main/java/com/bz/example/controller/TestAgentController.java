package com.bz.example.controller;

import com.bz.agent.executor.AgentExecutor;
import com.bz.agent.executor.QwenAgentExecutor;
import com.bz.agent.model.agent.AgentContext;
import com.bz.agent.model.agent.AgentOptions;
import com.bz.agent.model.agent.User;
import com.bz.agent.model.chat.ChatContext;
import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.service.AgentService;
import com.bz.example.tool.TestUtils;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/17
 */
@RestController
public class TestAgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<AgentChatResponse> generation(@RequestParam("userInput") String userInput) {
        ChatContext chatContext = ChatContext.builder()
                .userInput(userInput)
                .sessionId(UUID.randomUUID().toString())
                .botId(UUID.randomUUID().toString())
                .build();
        return agentService.stream(chatContext);
    }
}
