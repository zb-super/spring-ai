package com.bz.agent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionChatResponse implements AgentChatResponse{

    private Object data;

    private Integer index;

    @Override
    public Type getType() {
        return Type.ERROR;
    }
}
