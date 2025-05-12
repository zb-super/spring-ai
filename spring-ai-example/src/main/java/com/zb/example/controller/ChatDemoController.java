package com.zb.example.controller;

import com.zb.example.controller.tool.TestUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/11
 */
@RestController
public class ChatDemoController {

    private final ChatClient chatClient;

    private final OpenAiChatModel chatModel;


    public ChatDemoController(ChatClient.Builder chatClientBuilder, OpenAiChatModel chatModel) {
        this.chatClient = chatClientBuilder.build();
        this.chatModel = chatModel;
    }

    @GetMapping(value = "/ai")
    Flux<String> generation(@RequestParam("userInput") String userInput) {
        ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(ToolCallbacks.from(new TestUtils()))
                .internalToolExecutionEnabled(false)
                .build();

        Prompt prompt = new Prompt(
                List.of(new SystemMessage("You are a helpful assistant."), new UserMessage("今天天气怎么样")),
                chatOptions);


        Flux<ChatResponse> stream = chatModel.stream(prompt);


        return null;
    }
}
