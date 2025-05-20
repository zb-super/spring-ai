package com.bz.agent.client;

import com.bz.agent.executor.AgentExecutor;
import com.bz.agent.executor.QwenAgentExecutor;
import com.bz.agent.model.agent.AgentContext;
import com.bz.agent.model.agent.AgentOptions;
import com.bz.agent.model.agent.User;
import com.bz.agent.kb.KnowledgeBaseCallBack;
import com.bz.agent.kb.KnowledgeBaseOptions;
import com.bz.agent.model.response.AgentChatResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.ai.tool.ToolCallback;
import reactor.core.publisher.Flux;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/19
 */
@Data
@AllArgsConstructor
public class DefaultAgentChatClient {

    private String prompt;

    private User user;

    private AgentOptions chatOptions;

    private List<ToolCallback> toolCallbacks;

    private List<KnowledgeBaseCallBack> knowledgeBaseCallBacks;

    private KnowledgeBaseOptions knowledgeBaseOptions;

    private AgentExecutor executorClass;

    public Flux<AgentChatResponse> stream(){
        AgentContext context = AgentContext.builder()
                .user(user)
                .chatOptions(chatOptions)
                .prompt(prompt)
                .messages(new ArrayList<>())
                .knowledgeBaseCallBacks(knowledgeBaseCallBacks)
                .knowledgeBaseOptions(knowledgeBaseOptions)
                .callbacks(toolCallbacks)
                .build();
        return executorClass.chatStream(context);
    }

    public static DefaultAgentChatClientBuilder builder() {
        return new DefaultAgentChatClientBuilder();
    }

    public static class DefaultAgentChatClientBuilder {
        private String prompt = "";
        private User user;
        private AgentOptions chatOptions;
        private List<ToolCallback> toolCallbacks = new ArrayList<>();
        private List<KnowledgeBaseCallBack> knowledgeBaseCallBacks = new ArrayList<>();
        private KnowledgeBaseOptions knowledgeBaseOptions;
        private Class<? extends AgentExecutor> executorClass = QwenAgentExecutor.class;

        DefaultAgentChatClientBuilder() {
        }

        public DefaultAgentChatClientBuilder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public DefaultAgentChatClientBuilder user(User user) {
            Objects.requireNonNull(user, "user must not be null");
            this.user = user;
            return this;
        }

        public DefaultAgentChatClientBuilder chatOptions(AgentOptions chatOptions) {
            Objects.requireNonNull(chatOptions, "chatOptions must not be null");
            this.chatOptions = chatOptions;
            return this;
        }

        public DefaultAgentChatClientBuilder toolCallbacks(List<ToolCallback> toolCallbacks) {
            Objects.requireNonNull(toolCallbacks, "toolCallbacks must not be null");
            this.toolCallbacks = toolCallbacks;
            return this;
        }

        public DefaultAgentChatClientBuilder knowledgeBaseCallBacks(List<KnowledgeBaseCallBack> knowledgeBaseCallBacks) {
            Objects.requireNonNull(knowledgeBaseCallBacks, "knowledgeBaseCallBacks must not be null");
            this.knowledgeBaseCallBacks = knowledgeBaseCallBacks;
            return this;
        }

        public DefaultAgentChatClientBuilder knowledgeBaseOptions(KnowledgeBaseOptions knowledgeBaseOptions) {
            Objects.requireNonNull(knowledgeBaseOptions, "knowledgeBaseOptions must not be null");
            this.knowledgeBaseOptions = knowledgeBaseOptions;
            return this;
        }

        public DefaultAgentChatClientBuilder executorClass(Class<? extends AgentExecutor> executorClass) {
            Objects.requireNonNull(executorClass, "executorClass must not be null");
            this.executorClass = executorClass;
            return this;
        }

        public DefaultAgentChatClient build() throws Exception {
            Constructor<?> constructor = executorClass.getDeclaredConstructor();
            AgentExecutor executor  = (AgentExecutor) constructor.newInstance();
            return new DefaultAgentChatClient(this.prompt,
                    this.user,
                    this.chatOptions,
                    this.toolCallbacks,
                    this.knowledgeBaseCallBacks,
                    this.knowledgeBaseOptions,
                    executor);
        }
    }



}
