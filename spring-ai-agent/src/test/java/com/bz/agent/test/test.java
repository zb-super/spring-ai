package com.bz.agent.test;

import com.bz.agent.client.DefaultAgentChatClient;
import com.bz.agent.executor.QwenAgentExecutor;
import com.bz.agent.model.agent.AgentOptions;
import com.bz.agent.model.agent.User;
import com.bz.agent.model.response.AgentChatResponse;
import reactor.core.publisher.Flux;

import java.util.Arrays;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
public class test {
    public static void main(String[] args) {
        AgentOptions chatOptions = AgentOptions.builder()
                .apiKey("key")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
                .model("qwq-plus")
                .maxToken(1024)
                .build();

        User user = User.builder()
                .userInput("今天几号")
                .build();

        Flux<AgentChatResponse> agentChatResponseFlux = DefaultAgentChatClient.builder()
                .chatOptions(chatOptions)
                .user(user)
                .toolCallbacks(Arrays.asList())
                .executorClass(QwenAgentExecutor.class)
                .prompt("你是一个智能助手。")
                .knowledgeBaseCallBacks(Arrays.asList())
                .knowledgeBaseOptions(null)
                .build()
                .stream();

    }
}
