package com.bz.common.reactor.demo;

import reactor.core.publisher.Flux;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/6
 */
public class SubscribeDemo {
    public static void main(String[] args) {
        Flux.just(1, 3, 4,5,7,8,9,0)
                .subscribe(System.out::println,
                        er -> System.out.println(er.getMessage()),
                        () -> System.out.println("complete"),
                        subscription -> subscription.request(Long.MAX_VALUE));
    }
}
