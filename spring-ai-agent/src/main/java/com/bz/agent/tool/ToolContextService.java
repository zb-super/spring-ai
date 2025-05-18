package com.bz.agent.tool;

import java.util.List;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
public interface ToolContextService {

    public Object getBeanByToolName(String toolName);

    public List<String> getAllTool();
}
