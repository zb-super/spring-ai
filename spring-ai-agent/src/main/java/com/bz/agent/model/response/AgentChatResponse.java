package com.bz.agent.model.response;

public interface AgentChatResponse {

    Type getType();

    static AgentChatResponse ofThinkResponse(Object data){
        return new ThinkChatResponse(data);
    }

    static AgentChatResponse ofToolBeforeResponse(Object data){
        return new ToolBeforeChatResponse(data);
    }

    static AgentChatResponse ofTextResponse(Object data){
        return new ThinkChatResponse(data);
    }

    static AgentChatResponse ofErrorResponse(Object data){
        return new ThinkChatResponse(data);
    }

    enum Type {
        THINK,
        TOOL_BEFORE,
        TOOL_RESULT,
        ERROR,
        TEXT;
    }
}
