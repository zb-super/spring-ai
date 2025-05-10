package com.zb.reactor.demo;

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
//        Sinks.many().replay().
        Sinks.Many<Object> objectMany = Sinks.many().multicast()
                .onBackpressureBuffer(5);

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                objectMany.tryEmitNext(i);
            }
        }).start();

        Flux<Object> flux = objectMany.asFlux();
        flux.subscribe(System.out::println);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        flux.subscribe(item ->{
            System.out.println("v1  " + item);
        });

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
