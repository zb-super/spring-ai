package com.zb.reactor.demo;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/10
 */
public class CacheDemo {

    public static void main(String[] args) throws InterruptedException, IOException {
        Flux<Integer> cache = Flux.range(1, 30)
                .delayElements(Duration.ofSeconds(1))
                .cache(2);

        cache.subscribe(System.out::println);
        Thread.sleep(5000);

        cache.subscribe(System.out::println);
        System.in.read();
    }
}
