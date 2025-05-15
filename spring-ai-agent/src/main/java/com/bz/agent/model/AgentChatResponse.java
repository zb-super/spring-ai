package com.bz.agent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentChatResponse {

    private Object data;

    private Type type;

    public enum Type {
        THINK,
        TOOL,
        TEXT
    }

}
