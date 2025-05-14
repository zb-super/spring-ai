package com.bz.common.reactor.demo;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/6
 */
public class BaseSubscribeDemo {

    public static void main(String[] args) {

        Flux<Integer> just = Flux.just(1, 3, 4, 5, 7, 8, 9, 0);
        BaseSubscriber<Integer> subscriber = new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("hookOnSubscribe");
                requestUnbounded();
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println(value);
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("hookOnComplete");
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                System.out.println("hookOnError");
            }

            @Override
            protected void hookOnCancel() {
                System.out.println("hookOnCancel");
            }

            @Override
            protected void hookFinally(SignalType type) {
                System.out.println("hookFinally");
            }
        };

        just.subscribe(subscriber);

    }
}
