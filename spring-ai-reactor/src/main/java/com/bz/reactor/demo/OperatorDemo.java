package com.bz.reactor.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/8
 */
public class OperatorDemo {

    public static void main(String[] args) {
        // filter flatmap concatMap flatMapMany transform  defaultIfEmpty switchIfEmpty concat concatWith merge mergeWith mergeSeq zip zipWith
        OperatorDemo.testswitchIfEmpty();
    }

    public static void testswitchIfEmpty(){
        Flux.empty()
                .switchIfEmpty(Flux.just(1,2,3,4))
                .subscribe(System.out::println);

    }

    public static void testdefaultIfEmpty(){
        Flux.empty()
                .defaultIfEmpty(3)
                .subscribe(System.out::println);
    }



    public static void testZip(){
        Flux.zip(Flux.just(1,2), Flux.just(3,4))
                .subscribe(item ->{
                    Tuple2<Integer, Integer> item1 = item;
                });
    }

    public static void testconcatMap(){
        Flux.just(1, 2)
                .concatMap(item -> Flux.just(item,3,4))
                .subscribe(System.out::println);
    }

    public static void testconcatWith(){
        Flux.concat(Flux.just(1,3,4), Flux.just(2,4,5,6))
                .concatWith(Flux.just(1))
                .subscribe(System.out::println);
    }

    public static void testConcat(){
        Flux.concat(Flux.just(1,3,4), Flux.just(2,4,5,6))
                .subscribe(System.out::println);
    }


    public static void testTransformDeferred(){
        AtomicInteger integer = new AtomicInteger(0);
        Flux<Integer> integerFlux = Flux.range(1, 5)
                .transformDeferred(item -> {
                    if (integer.getAndIncrement() == 1){
                        return item.map(num -> num * 2);
                    }
                    return item;
                });

        integerFlux.subscribe(System.out::println);
        integerFlux.subscribe(System.out::println);
    }


    public static void testTransform(){
        AtomicInteger integer = new AtomicInteger(0);
        Flux<Integer> transform = Flux.range(1, 5)
                .transform(item -> {
                    if (integer.getAndIncrement() == 1){
                        return item.map(num -> num * 2);
                    }
                    return item;
                });
        transform.subscribe(System.out::println);
        transform.subscribe(System.out::println);
    }


    public static void testFlatMapSequential(){
        Flux.just("A", "B", "C")
                .flatMapSequential(s ->
                        Mono.fromCallable(() -> {

                            // 模拟耗时操作（如数据库查询）
                            Thread.sleep((long)(Math.random() * 1000));
                            return s.toLowerCase();
                        }).subscribeOn(Schedulers.boundedElastic())
                )
                .doOnNext(result -> System.out.println(Thread.currentThread().getName() +" Processed: " + result))
                .blockLast();

// 输出顺序始终为：a, b, c（即使处理时间随机）
    }

    public static void testFlatMapIterable(){
        List<List<Integer>> nestedLists = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
        );
        Flux.fromIterable(nestedLists)
                .flatMapIterable(list -> list)
                .subscribe(System.out::println);
    }

    public static void testFlatMap(){
        Flux.just("AA BB", "CC DD")
                .flatMap(item -> {
                    String[] split = item.split(" ");

                    return Flux.fromArray(split);
                })
                .subscribe(System.out::println);
    }

    public static void testFilter(){
        Flux<Integer> integerFlux = Flux.range(1, 10)
                .filter(num -> num > 5)
                .publishOn(Schedulers.boundedElastic())
                .filterWhen(num -> Mono.just(num.intValue() > 5));
        new Thread(() ->{
            integerFlux
                    .subscribe(System.out::println);
        }).start();

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
