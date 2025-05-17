package com.bz.agent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DelimiterChatResponse implements AgentChatResponse {

//    private Object data;

    @Override
    public Type getType() {
        return Type.DELIMITER;
    }

    @Override
    public Object getData() {
        return null;
    }
}
