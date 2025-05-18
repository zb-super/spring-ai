package com.bz.example.controller;

import com.bz.agent.executor.AgentExecutor;
import com.bz.agent.executor.QwenAgentExecutor;
import com.bz.agent.model.chat.AgentContext;
import com.bz.agent.model.chat.AgentOptions;
import com.bz.agent.model.chat.User;
import com.bz.agent.model.response.AgentChatResponse;
import com.bz.example.tool.TestUtils;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/17
 */
@RestController
public class TestAgentController {

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<AgentChatResponse> generation(@RequestParam("userInput") String userInput) {
        String key = "sk-0f38ec133e2c466089d90e7fc18fafcc";
        User user = User.builder()
                .userInput(userInput)
                .build();

        AgentOptions chatOptions = AgentOptions.builder()
                .apiKey(key)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
                .model("qwq-plus")
                .maxToken(1024)
                .build();



        AgentContext context = AgentContext.builder()
                .user(user)
                .chatOptions(chatOptions)
                .prompt("你是一个智能助手。")
                .messages(new ArrayList<>())
                .knowledgeBases(new ArrayList<>())
                .callbacks(ToolCallbacks.from(new TestUtils()))
                .build();

        AgentExecutor agentExecutor = new QwenAgentExecutor();
        return agentExecutor.chatStream(context)
                .filter(item -> item.getData() != null);
    }
}
