package com.bz.agent.test;

import com.bz.agent.executor.AgentExecutor;
import com.bz.agent.executor.QwenAgentExecutor;
import com.bz.agent.model.agent.AgentContext;
import com.bz.agent.model.agent.AgentOptions;
import com.bz.agent.model.agent.User;
import com.bz.agent.test.tool.TestUtils;
import org.springframework.ai.support.ToolCallbacks;


import java.io.IOException;
import java.util.ArrayList;

public class TestAgentOutput {
    public static void main(String[] args) {
        String key = "sk-0f38ec133e2c466089d90e7fc18fafcc";
        User user = User.builder()
                .userInput("今天天气怎么样？")
                .build();

        AgentOptions chatOptions = AgentOptions.builder()
                .apiKey(key)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
                .model("qwq-plus")
                .maxToken(1024)
                .build();

        AgentExecutor agentExecutor = new QwenAgentExecutor();
        AgentContext context = AgentContext.builder()
                .user(user)
                .chatOptions(chatOptions)
                .prompt("你是一个智能助手。")
                .messages(new ArrayList<>())
                .knowledgeBases(new ArrayList<>())
                .callbacks(ToolCallbacks.from(new TestUtils()))
                .build();
        agentExecutor.chatStream(context)
                .filter(item -> item.getData() != null)
                .subscribe(chat -> System.out.println(chat.getData()));
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
