package com.bz.agent.executor;

import com.bz.agent.model.chat.ChatContext;
import com.bz.agent.model.response.AgentChatResponse;
import com.bz.agent.tool.AgentToolCallingManager;
import com.bz.agent.tool.DefaultAgentToolCallingManager;
import io.micrometer.observation.ObservationRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.openai.inference.OpenAiInferenceChatModel;
import org.springframework.ai.openai.inference.api.OpenAiInferenceApi;
import org.springframework.ai.openai.inference.metadata.MsgMetadataType;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QwenAgentExecutor extends AbstractAgentExecutor implements AgentExecutor {

    private final AgentToolCallingManager toolCallingManager = DefaultAgentToolCallingManager.builder().build();

    @Override
    protected void doChatSteam(Prompt prompt, ChatModel chatModel, FluxSink<AgentChatResponse> emitter){
        doChatWithStream(0, prompt, chatModel, emitter);
    }

    private void doChatWithStream(int index, Prompt prompt, ChatModel chatModel, FluxSink<AgentChatResponse> emitter){
        chatModel.stream(prompt)
                .doOnNext(chatResponse -> sendMsg(index, chatResponse, emitter))
                .collectList()
                .subscribe(chatResponseList -> {
                    List<ChatResponse> toolList = new ArrayList<>();
                    StringBuilder thinkBuilder = new StringBuilder();
                    StringBuilder textBuilder = new StringBuilder();
                    processChatResponse(toolList, thinkBuilder, textBuilder, chatResponseList);
                    if (!toolList.isEmpty()) {
                        processFunctionCall(toolList, index, prompt, chatModel, emitter);
                    }else {
                        // 大模型总结的地方
                        emitter.next(AgentChatResponse.ofTextResponse(textBuilder.toString(), index));
                        // 停止符号
                        emitter.next(AgentChatResponse.ofStopResponse(index));
                    }
                });
    }

    private void processFunctionCall(List<ChatResponse> toolList,
                                     int index,
                                     Prompt prompt,
                                     ChatModel chatModel,
                                     FluxSink<AgentChatResponse> emitter){
        toolList.forEach(chatResponse -> {
            var toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse,
                    toolName -> sendFunctionCallName(index, toolName, emitter),
                    (toolName,result) ->{},
                    (toolName,ex) -> {}
            );
            if (toolExecutionResult.returnDirect()) {
                List<Generation> generations = ToolExecutionResult.buildGenerations(toolExecutionResult);
                for (Generation generation : generations) {
                    String text = generation.getOutput().getText();
                    emitter.next(AgentChatResponse.ofToolResultChatResponse(text, index));
                    emitter.next(AgentChatResponse.ofStopResponse(index));
                }
                // 停止符号
                emitter.next(AgentChatResponse.ofStopResponse(index));
            }else {
                doChatWithStream(index + 1 ,new Prompt(toolExecutionResult.conversationHistory(), prompt.getOptions()), chatModel, emitter);
            }
        });
    }

    private void processChatResponse(List<ChatResponse> toolList, StringBuilder thinkBuilder, StringBuilder textBuilder, List<ChatResponse> chatResponseList){
        for (ChatResponse chatResponse : chatResponseList) {
            if (chatResponse.hasToolCalls()){
                toolList.add(chatResponse);
                continue;
            }
            if (isBlank(chatResponse)){
                continue;
            }
            if (isThinkContext(chatResponse)){
                thinkBuilder.append(getContent(chatResponse));
            }else if (isTextContext(chatResponse)){
                textBuilder.append(getContent(chatResponse));
            }
        }
    }

    private void sendMsg(int index, ChatResponse chatResponse, FluxSink<AgentChatResponse> emitter){
        Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
        String text = chatResponse.getResult().getOutput().getText();
        Object type = metadata.get("type");
        if (type.equals(MsgMetadataType.THINK)) {
            emitter.next(AgentChatResponse.ofThinkResponse(text, index));
        }
    }

    private void sendFunctionCallName(int index, String toolName, FluxSink<AgentChatResponse> emitter){
        emitter.next(AgentChatResponse.ofToolBeforeResponse(toolName,index));
    }

    private boolean isBlank(ChatResponse chatResponse){
        String text = chatResponse.getResult().getOutput().getText();
        return StringUtils.isBlank(text);
    }

    private boolean isThinkContext(ChatResponse chatResponse){
        Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
        Object type = metadata.get("type");
        return type.equals(MsgMetadataType.THINK);
    }

    private boolean isTextContext(ChatResponse chatResponse){
        Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
        Object type = metadata.get("type");
        return type.equals(MsgMetadataType.TEXT);
    }

    public String getContent(ChatResponse chatResponse){
        return chatResponse.getResult().getOutput().getText();
    }

    @Override
    protected ChatModel buildChatModel(ChatContext context){
        OpenAiInferenceApi openAiApi = OpenAiInferenceApi.builder()
                .apiKey(context.getChatOptions().getApiKey())
                .baseUrl(context.getChatOptions().getBaseUrl())
                .build();

        return OpenAiInferenceChatModel.builder()
                .openAiApi(openAiApi)
                .toolCallingManager(toolCallingManager)
                .retryTemplate(RetryTemplate.defaultInstance())
                .observationRegistry(ObservationRegistry.create())
                .toolExecutionEligibilityPredicate(new DefaultToolExecutionEligibilityPredicate())
                .build();
    }
}
