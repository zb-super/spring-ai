package com.bz.agent.executor;

import com.bz.agent.model.ChatContext;
import com.bz.agent.model.AgentChatResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import reactor.core.publisher.FluxSink;

import java.util.Objects;

public class QwenExecutor extends AbstractAgentExecutor implements AgentExecutor {

    private final ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

    @Override
    protected void doChatSteam(Prompt prompt, ChatModel chatModel, FluxSink<AgentChatResponse> emitter){
        Objects.requireNonNull(chatModel.stream(prompt)
                        .doOnNext(item -> {
                            String text = item.getResult().getOutput().getText();
                            emitter.next(new AgentChatResponse(text, AgentChatResponse.Type.TEXT));
                        })
                        .collectList()
                        .block())
                .stream()
                .filter(ChatResponse::hasToolCalls)
                .map(chatResponse ->
                        toolCallingManager.executeToolCalls(prompt, chatResponse).conversationHistory())
                .forEach(messages -> doChatSteam(new Prompt(messages, prompt.getOptions()), chatModel, emitter));
    }

    @Override
    protected ChatModel buildChatModel(ChatContext context){
        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(context.getChatOptions().getApiKey())
                .baseUrl(context.getChatOptions().getBaseUrl())
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .toolCallingManager(toolCallingManager)
                .build();
    }
}
