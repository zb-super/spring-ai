package com.bz.agent.service;

import com.bz.agent.model.chat.ChatContext;
import com.bz.agent.model.response.AgentChatResponse;
import reactor.core.publisher.Flux;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
public interface AgentService {
    Flux<AgentChatResponse> stream(ChatContext chatContext);

}
