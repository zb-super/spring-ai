package com.bz.common.reactor.demo;

import reactor.core.publisher.Flux;

public class TwoSubscribe {
    public static void main(String[] args) {
        Flux<Integer> range = Flux.range(1, 10);
        range.subscribe(item ->{
            System.out.println("线程名称：" + Thread.currentThread().getName() + "值" +item);
        });
        range.subscribe(item ->{
            System.out.println("线程名称：" + Thread.currentThread().getName() + "值" +item);
        });
    }
}
