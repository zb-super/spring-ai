package com.bz.agent.model.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户输入
     */
    private String userInput;


    private String sessionId;


    public String botId;


    private String questionId;
}
