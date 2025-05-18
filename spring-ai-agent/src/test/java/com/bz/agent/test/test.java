package com.bz.agent.test;

import com.bz.agent.executor.ExecutorContext;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
public class test {
    public static void main(String[] args) {
        ExecutorContext context = new ExecutorContext();
        int andAdd = context.getAndAdd();
        System.out.println(andAdd);
    }
}
