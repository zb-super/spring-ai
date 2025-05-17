package com.bz.agent.test;

import com.bz.agent.executor.AgentExecutor;
import com.bz.agent.executor.QwenAgentExecutor;
import com.bz.agent.model.chat.ChatContext;
import com.bz.agent.model.chat.ChatOptions;
import com.bz.agent.model.chat.User;
import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.test.tool.TestUtils;
import org.reactivestreams.Subscription;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.inference.api.OpenAiInferenceApi;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.SignalType;
import reactor.util.context.Context;


import java.io.IOException;
import java.util.ArrayList;

public class TestAgentOutput {
    public static void main(String[] args) {
        String key = "sk-0f38ec133e2c466089d90e7fc18fafcc";

        User user = User.builder()
                .userInput("今天天气怎么样？")
                .build();

        ChatOptions chatOptions = ChatOptions.builder()
                .apiKey(key)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
                .model("qwq-plus")
                .maxToken(1024)
                .build();

        AgentExecutor agentExecutor = new QwenAgentExecutor();
        ChatContext context = ChatContext.builder()
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
