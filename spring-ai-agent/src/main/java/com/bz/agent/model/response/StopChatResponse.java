package com.bz.agent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopChatResponse implements AgentChatResponse{

//    private Object data;

    private Integer index;

    @Override
    public Type getType() {
        return Type.STOP;
    }

    @Override
    public Object getData() {
        return null;
    }
}
