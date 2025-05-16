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
        /**
         * 思考内容
         */
        THINK,
        /**
         * 调用工具的消息
         */
        TOOL_BEFORE,
        /**
         * 工具的返回结果
         */
        TOOL_RESULT,
        /**
         * 报错结果
         */
        ERROR,
        /**
         * 大模型回答的正文
         */
        TEXT,
        /**
         * 各类消息，分隔符号
         */
        DELIMITER,
        /**
         * 停止信号
         */
        STOP
    }
}
