package com.bz.common.reactor.demo;

import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/12
 */
public class BlocApi {
    public static void main(String[] args) {
        List<Integer> block = Flux.range(1, 10)
                .doOnNext(System.out::println)
                .collectList()
                .block();
        System.out.println(1);


    }
}
