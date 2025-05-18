package com.bz.example.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/11
 */
public class TestUtils {

    @Tool(description = "获取当前时间")
    String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String customFormat = now.format(formatter);
        return customFormat;
    }

    @Tool(description = "获取天气预报")
    String getWeather(@ToolParam(description = "时间") String time){
        return "雨夹雪";
    }

    @Tool(description = "查询用户信息", returnDirect = true)
    Map<String, Object> findUserInfo(@ToolParam(description = "名字") String name){
        return Map.of("name","zb","age",12);
    }

}
