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
        return new TextChatResponse(data);
    }

    static AgentChatResponse ofErrorResponse(Object data){
        return new ExceptionChatResponse(data);
    }

    static AgentChatResponse ofDelimiterResponse(Object data){
        return new DelimiterChatResponse(data);
    }

    static AgentChatResponse ofStopResponse(Object data){
        return new StopChatResponse(data);
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
