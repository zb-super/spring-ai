package com.bz.common.reactor.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/10
 */
public class SinksDemo {

    public static void main(String[] args) {
        Sinks.Many<Object> objectMany = Sinks.many().multicast()
                .onBackpressureBuffer(5);

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                objectMany.tryEmitNext(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            objectMany.tryEmitComplete();
        }).start();

        Flux<Object> flux = objectMany.asFlux();
        flux.subscribe(item -> {
            System.out.println(item);
            System.out.println(Thread.currentThread().getName());
        });

//        try {
//            System.in.read();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }
}
