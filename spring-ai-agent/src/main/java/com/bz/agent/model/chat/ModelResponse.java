package com.bz.agent.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelResponse {

    private String think;

    private String content;

    private List<ChatResponse> toolList;


    public boolean isNeedExecTool(){
        return toolList != null && !toolList.isEmpty();
    }

}
