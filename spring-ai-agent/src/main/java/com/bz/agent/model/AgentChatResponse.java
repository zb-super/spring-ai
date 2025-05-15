package com.bz.agent.model;

public interface AgentChatResponse {

    Type getType();

    static AgentChatResponse ofThinkResponse(Object data){
        return new ThinkChatResponse(data);
    }

    static AgentChatResponse ofToolResponse(Object data){
        return new ThinkChatResponse(data);
    }

    static AgentChatResponse ofTextResponse(Object data){
        return new ThinkChatResponse(data);
    }

    static AgentChatResponse ofErrorResponse(Object data){
        return new ThinkChatResponse(data);
    }

    enum Type {
        THINK,
        TOOL,
        TEXT,
        ERROR
    }
}
