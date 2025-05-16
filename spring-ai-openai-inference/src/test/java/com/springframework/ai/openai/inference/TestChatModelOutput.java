package com.springframework.ai.openai.inference;

import io.micrometer.observation.ObservationRegistry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.inference.OpenAiInferenceChatModel;
import org.springframework.ai.openai.inference.api.OpenAiInferenceApi;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestChatModelOutput {
    public static void main(String[] args) {
        String key = "sk-0f38ec133e2c466089d90e7fc18fafcc";

        ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

        OpenAiInferenceApi openAiApi = OpenAiInferenceApi.builder()
                .apiKey(key)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
                .build();

        OpenAiInferenceChatModel chatModel = OpenAiInferenceChatModel.builder()
                .openAiApi(openAiApi)
                .toolCallingManager(toolCallingManager)
                .retryTemplate(RetryTemplate.defaultInstance())
                .observationRegistry(ObservationRegistry.create())
                .toolExecutionEligibilityPredicate(new DefaultToolExecutionEligibilityPredicate())
                .build();

        ToolCallingChatOptions chatOptions = ToolCallingChatOptions.builder()
                .internalToolExecutionEnabled(false)
                .model("qwq-plus")
                .maxTokens(1024)
                .build();

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        StringBuilder thinking = new StringBuilder();
        StringBuilder content = new StringBuilder();
        chatModel.stream(new Prompt("你好你是谁", chatOptions))
                .filter(chatResponse -> {
                    String text = chatResponse.getResult().getOutput().getText();
                    return StringUtils.isNotBlank(text);
                }).map(chatResponse -> {
                    Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
                    String text = chatResponse.getResult().getOutput().getText();
                    if (metadata.containsKey("type")){
                        return Msg.builder().text(text).type("thinking").build();
                    }else {
                        return Msg.builder().text(text).type("text").build();
                    }
                }).groupBy(Msg::getType);

//                .subscribe(chatResponse -> {
//                    String text = chatResponse.getResult().getOutput().getText();
//                    if (StringUtils.isNotBlank(text)) {
//                        Map<String, Object> metadata = chatResponse.getResult().getOutput().getMetadata();
//                        if (metadata.containsKey("type")){
//                            thinking.append(text);
//                        }else {
//                            content.append(text);
//                        }
//                    }
//                }, ex -> {
//
//                },() -> {
////                    System.out.println(thinking.toString());
////                    System.out.println("------------------------------------------------");
////                    System.out.println(content.toString());
//                });
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Msg{
        private String text;
        private String type;
    }
}
