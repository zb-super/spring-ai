package com.bz.agent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelimiterChatResponse implements AgentChatResponse {

//    private Object data;

    private Integer index;

    @Override
    public Type getType() {
        return Type.DELIMITER;
    }

    @Override
    public Object getData() {
        return null;
    }
}
