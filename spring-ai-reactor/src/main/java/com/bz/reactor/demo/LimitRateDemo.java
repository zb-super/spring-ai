package com.bz.reactor.demo;

import reactor.core.publisher.Flux;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/7
 */
public class LimitRateDemo {

    public static void main(String[] args) {
        Flux.range(1, 100)
                .log()
                .limitRate(10)
                .subscribe();
    }
}
