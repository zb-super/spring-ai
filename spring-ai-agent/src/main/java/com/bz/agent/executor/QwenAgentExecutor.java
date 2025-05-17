package com.bz.agent.executor;

import com.bz.agent.model.chat.ChatContext;
import com.bz.agent.model.response.AgentChatResponse;
import io.micrometer.observation.ObservationRegistry;
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
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

public class QwenAgentExecutor extends AbstractAgentExecutor implements AgentExecutor {

    private final ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

    @Override
    protected void doChatSteam(Prompt prompt, ChatModel chatModel, FluxSink<AgentChatResponse> emitter){
        doChatWithStream(0, prompt, chatModel, emitter);
    }

    private void doChatWithStream(int num, Prompt prompt, ChatModel chatModel, FluxSink<AgentChatResponse> emitter){
        chatModel.stream(prompt)
                .doOnNext(chatResponse -> sendMsg(chatResponse, emitter))
                .collectList()
                .subscribe(chatResponseList -> {
                    System.out.println(Thread.currentThread().getName() + " =>>>>>>>>sub");
//                    List<ChatResponse> list = chatResponseList.stream().toList();
//                    list.stream().forEach(chatResponse1 -> {
//                        var toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse1);
//                        if (toolExecutionResult.returnDirect()) {
//                            emitter.next(AgentChatResponse.ofStopResponse());
//                        }else {
//                            doChatWithStream(num + 1 ,new Prompt(toolExecutionResult.conversationHistory(), prompt.getOptions()), chatModel, emitter);
//                        }
//                    });
                });
    }

    private void sendMsg(ChatResponse chatResponse, FluxSink<AgentChatResponse> emitter){
        Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
        String text = chatResponse.getResult().getOutput().getText();
        Object type = metadata.get("type");
        if (type.equals(MsgMetadataType.THINK)) {
            emitter.next(AgentChatResponse.ofThinkResponse(text));
        } else {
            emitter.next(AgentChatResponse.ofTextResponse(text));
        }
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
