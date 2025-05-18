package com.bz.agent.service.impl;

import com.bz.agent.executor.AgentExecutor;
import com.bz.agent.model.agent.AgentContext;
import com.bz.agent.model.agent.AgentOptions;
import com.bz.agent.model.agent.User;
import com.bz.agent.model.chat.ChatContext;
import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.service.AgentService;
import com.bz.agent.tool.ToolContextService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
@Service
public class AgentServiceImpl implements AgentService {

    private AgentExecutor agentExecutor = AgentExecutor.ofQwenAgentExecutor();

    @Autowired
    private ToolContextService contextService;

    @Override
    public Flux<AgentChatResponse> stream(ChatContext chatContext) {
        String questionId = saveMsg(chatContext);
        String sessionId = chatContext.getSessionId();
        AgentContext agentContext = builderContext(chatContext);
        Flux<AgentChatResponse> agentChatResponseFlux = agentExecutor.chatStream(agentContext)
                .doOnNext(chat -> {
                    chat.setQuestionId(questionId).setSessionId(sessionId);
                });
        return agentChatResponseFlux;
    }

    // from db
    private AgentContext builderContext(ChatContext chatContext){
        String key = "sk-0f38ec133e2c466089d90e7fc18fafcc";
        User user = User.builder()
                .userInput(chatContext.getUserInput())
                .build();

        AgentOptions chatOptions = AgentOptions.builder()
                .apiKey(key)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
                .model("qwq-plus")
                .maxToken(1024)
                .build();

        Set<String> tools = Set.of("getCurrentDateTime");
        List<Object> beans = tools.stream().map(name -> contextService.getBeanByToolName(name)).toList();
        ToolCallback[] from = ToolCallbacks.from(beans.toArray());
        ToolCallback[] array = Arrays.stream(from).filter(item -> {
            String name = item.getToolDefinition().name();
            return tools.contains(name);
        }).toList().toArray(new ToolCallback[]{});

        AgentContext context = AgentContext.builder()
                .user(user)
                .chatOptions(chatOptions)
                .prompt("你是一个智能助手。")
                .messages(new ArrayList<>())
                .knowledgeBases(new ArrayList<>())
                .callbacks(array)
                .build();

        return context;
    }

    private String saveMsg(ChatContext chatContext){
        return UUID.randomUUID().toString();
    }
}
