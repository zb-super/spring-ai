package com.bz.agent.model;

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


    /**
     * 会话Id
     */
    private String sessionId;
}
