package com.bz.common.reactor.demo;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.io.IOException;
import java.time.Duration;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/17
 */
public class GroupByDemo {

    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 3, 5, 2, 4, 6).delayElements(Duration.ofSeconds(1));
        Flux<GroupedFlux<Boolean, Integer>> groupedFlux = numbers.groupBy(n -> n % 2 == 0);

        groupedFlux.subscribe(new BaseSubscriber<GroupedFlux<Boolean, Integer>>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            protected void hookOnNext(GroupedFlux<Boolean, Integer> value) {
                value.subscribe(item -> {
                    System.out.print(item);
                });
            }


            @Override
            protected void hookOnComplete() {

            }
        });




        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
