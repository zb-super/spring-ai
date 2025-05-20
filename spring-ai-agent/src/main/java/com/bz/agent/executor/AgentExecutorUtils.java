package com.bz.agent.executor;

import com.bz.agent.model.chat.ModelResponse;
import com.bz.agent.model.response.AgentChatResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.inference.metadata.MsgMetadataType;
import org.springframework.ai.tool.execution.ToolExecutionException;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgentExecutorUtils {

    /**
     * 处理聊天内容
     * @param chatResponseList
     */
    public static ModelResponse processChatResponse(List<ChatResponse> chatResponseList){
        List<ChatResponse> toolList = new ArrayList<>();
        StringBuilder thinkBuilder = new StringBuilder();
        StringBuilder textBuilder = new StringBuilder();

        for (ChatResponse chatResponse : chatResponseList) {
            if (chatResponse.hasToolCalls()){
                toolList.add(chatResponse);
                continue;
            }
            if (isBlank(chatResponse)){
                continue;
            }
            if (isTypeContext(chatResponse, MsgMetadataType.THINK)){
                thinkBuilder.append(getContentText(chatResponse));
            }else if (isTypeContext(chatResponse, MsgMetadataType.TEXT)){
                textBuilder.append(getContentText(chatResponse));
            }
        }

        return ModelResponse.builder()
                .toolList(toolList)
                .think(thinkBuilder.toString())
                .content(textBuilder.toString())
                .build();
    }

    /**
     * 判断聊天内容是否为空
     * @param chatResponse
     * @return
     */
    public static boolean isBlank(ChatResponse chatResponse){
        String text = chatResponse.getResult().getOutput().getText();
        return StringUtils.isBlank(text);
    }


    /**
     * 类型匹配
     * @param chatResponse
     * @param target
     * @return
     */
    public static boolean isTypeContext(ChatResponse chatResponse, MsgMetadataType target){
        Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
        Object type = metadata.get("type");
        return type.equals(target);
    }

    /**
     * 获取内容
     * @param chatResponse
     * @return
     */
    public static String getContentText(ChatResponse chatResponse){
        return chatResponse.getResult().getOutput().getText();
    }

    /**
     * 发送思考内容
     * @param index
     * @param chatResponse
     * @param emitter
     */
    public static void sendThinkMsg(int index, ChatResponse chatResponse, FluxSink<AgentChatResponse> emitter){
        String text = chatResponse.getResult().getOutput().getText();
        if (isTypeContext(chatResponse, MsgMetadataType.THINK) && StringUtils.isNotBlank(text)) {
            emitter.next(AgentChatResponse.ofThinkResponse(text, index));
        }
    }

    public static void sendFunctionCallName(int index, String toolName, FluxSink<AgentChatResponse> emitter){
        emitter.next(AgentChatResponse.ofToolBeforeResponse(toolName,index));
    }

    public static void sendFunctionError(int index, String toolName, ToolExecutionException ex, FluxSink<AgentChatResponse> emitter){
        emitter.next(AgentChatResponse.ofErrorResponse("调用工具出错" + toolName +"错误信息：" + ex.getMessage() +"。", index));
    }
}
