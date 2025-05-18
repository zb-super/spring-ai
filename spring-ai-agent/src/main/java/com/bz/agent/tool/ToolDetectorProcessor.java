package com.bz.agent.tool;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
@Component
public class ToolDetectorProcessor implements BeanPostProcessor,ToolContextService {

    private final Map<String, Object> beans = new HashMap<>();

    private final List<String> tools = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        for (Method method : targetClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Tool.class)) {
                Tool toolAnnotation = method.getAnnotation(Tool.class);
                String toolName = toolAnnotation.name();
                if (StringUtils.isBlank(toolName)){
                    toolName = method.getName();

                }
                beans.put(toolName, bean);
                tools.add(toolName);
            }
        }
        return bean;
    }

    @Override
    public Object getBeanByToolName(String toolName) {
        return beans.get(toolName);
    }

    @Override
    public List<String> getAllTool() {
        return tools;
    }
}
