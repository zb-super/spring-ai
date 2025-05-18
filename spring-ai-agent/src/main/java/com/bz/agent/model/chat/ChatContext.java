package com.bz.agent.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatContext {

    private String userInput;

    private String botId;

    private String sessionId;
}
