package com.bz.common.reactor.demo;

import reactor.core.publisher.Flux;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/6
 */
public class ContractDemo {
    public static void main(String[] args) {
        Flux.just(1, 3, 4)
                .concatWith(Flux.just(4, 5, 6, 7, 8))
                .map(item ->  item*2)
                .filter(item -> item > 14)
                .log()
                .subscribe(System.out::println);

    }
}
