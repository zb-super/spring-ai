package com.bz.agent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor()
public class DelimiterChatResponse implements AgentChatResponse {

//    private Object data;

    private Integer index;

    private String sessionId;

    private String questionId;

    @Override
    public Type getType() {
        return Type.DELIMITER;
    }

    @Override
    public Object getData() {
        return null;
    }

    public AgentChatResponse setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public AgentChatResponse setQuestionId(String questionId) {
        this.questionId = questionId;
        return this;
    }

}
