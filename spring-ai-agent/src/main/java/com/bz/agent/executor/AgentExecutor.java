package com.bz.agent.executor;

import com.bz.agent.model.agent.AgentContext;
import com.bz.agent.model.response.AgentChatResponse;
import reactor.core.publisher.Flux;

public interface AgentExecutor {

    /**
     * 智能体聊天入口
     * @param context 聊天上下文
     * @return
     */
    Flux<AgentChatResponse> chatStream(AgentContext context);

    static AgentExecutor ofQwenAgentExecutor(){
        return new QwenAgentExecutor();
    }
}
