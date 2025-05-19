package com.bz.agent.client;

import com.bz.agent.executor.AgentExecutor;
import com.bz.agent.model.agent.AgentOptions;
import com.bz.agent.model.agent.User;
import com.bz.agent.model.kb.KnowledgeBaseCallBack;
import com.bz.agent.model.kb.KnowledgeBaseOptions;
import com.bz.agent.model.response.AgentChatResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import org.springframework.ai.tool.ToolCallback;
import reactor.core.publisher.Flux;

import java.util.List;

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

    private Class<? extends AgentExecutor> executorClass;

    public Flux<AgentChatResponse> stream(){

        return null;
    }


    public static DefaultAgentChatClientBuilder builder() {
        return new DefaultAgentChatClientBuilder();
    }

    public static class DefaultAgentChatClientBuilder {
        private String prompt;
        private User user;
        private AgentOptions chatOptions;
        private List<ToolCallback> toolCallbacks;
        private List<KnowledgeBaseCallBack> knowledgeBaseCallBacks;
        private KnowledgeBaseOptions knowledgeBaseOptions;
        private Class<? extends AgentExecutor> executorClass;

        DefaultAgentChatClientBuilder() {
        }

        public DefaultAgentChatClientBuilder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public DefaultAgentChatClientBuilder user(User user) {
            this.user = user;
            return this;
        }

        public DefaultAgentChatClientBuilder chatOptions(AgentOptions chatOptions) {
            this.chatOptions = chatOptions;
            return this;
        }

        public DefaultAgentChatClientBuilder toolCallbacks(List<ToolCallback> toolCallbacks) {
            this.toolCallbacks = toolCallbacks;
            return this;
        }

        public DefaultAgentChatClientBuilder knowledgeBaseCallBacks(List<KnowledgeBaseCallBack> knowledgeBaseCallBacks) {
            this.knowledgeBaseCallBacks = knowledgeBaseCallBacks;
            return this;
        }

        public DefaultAgentChatClientBuilder knowledgeBaseOptions(KnowledgeBaseOptions knowledgeBaseOptions) {
            this.knowledgeBaseOptions = knowledgeBaseOptions;
            return this;
        }

        public DefaultAgentChatClientBuilder executorClass(Class<? extends AgentExecutor> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public DefaultAgentChatClient build() {
            return new DefaultAgentChatClient(this.prompt, this.user, this.chatOptions, this.toolCallbacks, this.knowledgeBaseCallBacks, this.knowledgeBaseOptions, this.executorClass);
        }
    }



}
