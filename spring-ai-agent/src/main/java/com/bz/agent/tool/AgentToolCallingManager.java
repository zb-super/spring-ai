package com.bz.agent.tool;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.execution.ToolExecutionException;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
public interface AgentToolCallingManager extends ToolCallingManager {

    /**
     *
     * @param prompt
     * @param chatResponse
     * @param pre 执行前
     * @param after 执行后
     * @param exception 异常时
     * @return
     */
    ToolExecutionResult executeToolCalls(Prompt prompt, ChatResponse chatResponse,
                                         Consumer<String> pre,
                                         BiConsumer<String,String> after,
                                         BiConsumer<String,ToolExecutionException> exception);
}
