package com.bz.agent.executor;

import com.bz.agent.model.chat.ChatContext;
import com.bz.agent.model.response.AgentChatResponse;
import reactor.core.publisher.Flux;

public interface AgentExecutor {

    /**
     * 智能体聊天入口
     * @param context 聊天上下文
     * @return
     */
    public Flux<AgentChatResponse> chatStream(ChatContext context);
}
