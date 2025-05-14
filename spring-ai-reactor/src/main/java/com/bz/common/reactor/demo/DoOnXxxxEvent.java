package com.bz.common.reactor.demo;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class DoOnXxxxEvent {
    public static void main(String[] args) {

        Flux<Integer> integerFlux = Flux.range(1, 10)
                .map(item -> item * 2)
                .filter(value -> value > 10)
                .doOnDiscard(Integer.class, item -> {
                    System.out.println("doOnDiscard====>" + item.intValue());
                })
                .doOnTerminate(() -> {
                    System.out.println("doOnTerminate");
                })
                .doOnRequest(value -> {
                    System.out.println("doOnRequest====>" + value);
                })
                .doOnCancel(() -> {
                    System.out.println("doOnCancel");
                })
                .doOnNext(item -> {
                    System.out.println("doOnNext====>" + item);
                })
                .doOnError(ex -> {
                    System.out.println("doOnError ===>" + ex.getMessage());
                })
                .doOnEach(item -> {
                    System.out.println("doOnEach====>" + item.toString());
                })
                .doOnComplete(() -> {
                    System.out.println("doOnComplete");
                })
                .doOnSubscribe(item -> {
                    System.out.println("doOnSubscribe ===>" + item.toString());
                });


        integerFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("hookOnSubscribe ===>" + subscription.toString());
                request(1);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println("hookOnNext ===>" + value);
                if (value == 14){
                    cancel();
                }
                request(1);
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("hookOnComplete");
            }

            @Override
            protected void hookOnCancel() {
                System.out.println("hookOnCancel");
            }
        });

    }
}
