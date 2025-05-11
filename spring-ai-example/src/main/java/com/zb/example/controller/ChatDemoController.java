package com.zb.example.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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

    public ChatDemoController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai")
    Flux<String> generation(@RequestParam("userInput") String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .stream()
                .content();
    }
}
