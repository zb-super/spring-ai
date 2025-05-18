package com.bz.agent.model.response;

public interface AgentChatResponse {

    Type getType();

    Integer getIndex();

    public Object getData();

    public AgentChatResponse setSessionId(String sessionId);

    public AgentChatResponse setQuestionId(String questionId);

    static AgentChatResponse ofThinkResponse(Object data, Integer index){
        return new ThinkChatResponse(data, index, null, null);
    }

    static AgentChatResponse ofToolBeforeResponse(Object data, Integer index){
        return new ToolBeforeChatResponse(data, index, null, null);
    }

    static AgentChatResponse ofToolResultChatResponse(Object data, Integer index){
        return new ToolResultChatResponse(data, index, null, null);
    }

    static AgentChatResponse ofTextResponse(Object data, Integer index){
        return new TextChatResponse(data, index, null, null);
    }

    static AgentChatResponse ofErrorResponse(Object data, Integer index){
        return new ExceptionChatResponse(data, index,null, null);
    }

    static AgentChatResponse ofDelimiterResponse(Integer index){
        return new DelimiterChatResponse(index,null, null);
    }

    static AgentChatResponse ofStopResponse(Integer index){
        return new StopChatResponse(index,null, null);
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
