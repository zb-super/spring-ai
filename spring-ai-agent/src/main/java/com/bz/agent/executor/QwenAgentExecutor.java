package com.bz.agent.executor;

import com.bz.agent.model.chat.ChatContext;
import com.bz.agent.model.response.AgentChatResponse;
import io.micrometer.observation.ObservationRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.inference.OpenAiInferenceChatModel;
import org.springframework.ai.openai.inference.api.OpenAiInferenceApi;
import org.springframework.ai.openai.inference.metadata.MsgMetadataType;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QwenAgentExecutor extends AbstractAgentExecutor implements AgentExecutor {

    private final ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

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

                    for (ChatResponse chatResponse : chatResponseList) {
                        if (chatResponse.hasToolCalls()){
                            toolList.add(chatResponse);
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
                    if (!toolList.isEmpty()) {
                        toolList.stream().forEach(chatResponse -> {
                            sendFunctionCallName(index, chatResponse, emitter) ;
                            var toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse);
                            if (toolExecutionResult.returnDirect()) {
//                                emitter.next(AgentChatResponse.ofStopResponse(index));
                            }else {
                                doChatWithStream(index + 1 ,new Prompt(toolExecutionResult.conversationHistory(), prompt.getOptions()), chatModel, emitter);
                            }
                        });
                    }else {
                        // 大模型总结的地方
                        emitter.next(AgentChatResponse.ofTextResponse(textBuilder.toString(), index));
                    }
                });
    }

    private void sendMsg(int index, ChatResponse chatResponse, FluxSink<AgentChatResponse> emitter){
        Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
        String text = chatResponse.getResult().getOutput().getText();
        Object type = metadata.get("type");
        if (type.equals(MsgMetadataType.THINK)) {
            emitter.next(AgentChatResponse.ofThinkResponse(text, index));
        }
    }

    private void sendFunctionCallName(int index, ChatResponse chatResponse, FluxSink<AgentChatResponse> emitter){
        chatResponse.getResult().getOutput().getToolCalls().forEach(item -> {
            emitter.next(AgentChatResponse.ofToolBeforeResponse(item.name(),index));
        });
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
