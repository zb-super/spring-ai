package com.bz.agent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.tool.ToolCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatContext {

    /**
     * 用户输入的信息
     */
    private User user;

    /**
     * 附加属性，供工具使用
     */
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * 聊天记录
     */
    private List<Message> messages;

    /**
     * 聊天选项
     */
    private ChatOptions chatOptions;

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 知识库
     */
    private List<KnowledgeBase> knowledgeBases;

    /**
     * 工具
     */
    private ToolCallback[] callbacks;
}
