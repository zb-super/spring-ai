package com.bz.agent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolBeforeChatResponse implements AgentChatResponse{

    private Object data;

    private Integer index;

    @Override
    public Type getType() {
        return Type.TOOL_BEFORE;
    }
}
