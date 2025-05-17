package com.bz.agent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StopChatResponse implements AgentChatResponse{

//    private Object data;

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public Object getData() {
        return null;
    }
}
