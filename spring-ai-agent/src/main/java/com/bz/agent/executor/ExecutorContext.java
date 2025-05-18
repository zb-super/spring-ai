package com.bz.agent.executor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorContext {

    private int index;

    private Prompt prompt;

    private ChatModel chatModel;

    public ExecutorContext addIndex(){
        this.index = index + 1 ;
        return this;
    }
}
