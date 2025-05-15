package com.bz.agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThinkChatResponse implements AgentChatResponse{

    private Object data;

    @Override
    public Type getType() {
        return Type.THINK;
    }
}
