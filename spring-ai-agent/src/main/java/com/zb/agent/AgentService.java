package com.zb.agent;

import com.zb.agent.model.AgentMsg;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import reactor.core.publisher.Flux;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/12
 */
public interface AgentService {

    public Flux<AgentMsg> startTask(String query, ChatModel chatModel, ToolCallback[] callbacks);
}
