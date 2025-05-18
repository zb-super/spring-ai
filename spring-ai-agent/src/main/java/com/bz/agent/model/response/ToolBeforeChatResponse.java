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

    private String sessionId;

    private String questionId;

    @Override
    public Type getType() {
        return Type.TOOL_BEFORE;
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
