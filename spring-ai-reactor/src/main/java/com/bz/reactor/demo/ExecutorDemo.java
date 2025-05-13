package com.bz.reactor.demo;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

public class ExecutorDemo {

    public static void main(String[] args) throws InterruptedException {
//        Scheduler scheduler1 = Schedulers.boundedElastic();
//        Scheduler scheduler2 = Schedulers.newParallel("Parallel");
//        Flux.range(1, 10)
//                .map(item -> item * 2)
//                .log()
//                .publishOn(scheduler1)
//                .map(item -> item * 3)
////                .log()
//                .subscribeOn(scheduler1)
//                .subscribe(integer -> System.out.println(Thread.currentThread().getName() + ": " + integer));
        ExecutorDemo.fluxTest();
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void fluxTest() throws InterruptedException {
        final Random random = new Random();
        Flux.create(sink -> {
                    ArrayList<Integer> list = new ArrayList<>();
                    Integer i = 0;
                    while (list.size() != 10) {
                        int value = random.nextInt(100);
                        list.add(value);
                        i += 1;
                        System.out.println(Thread.currentThread().getName() + "发射了元素" + i);
                        sink.next(value);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sink.complete();
                })
                .log()
                .doOnRequest(x -> System.out.println("..." + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.boundedElastic(), false)
                .publishOn(MyScheduler(), 4)
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), "消费了元素"))
                .subscribe(item ->{
                    System.out.println(Thread.currentThread().getName());
                });
        Thread.sleep(10000);
    }

    public static Scheduler MyScheduler() {
        Executor executor = new ThreadPoolExecutor(
                10,  //corePoolSize
                10,  //maximumPoolSize
                0L, TimeUnit.MILLISECONDS, //keepAliveTime, unit
                new LinkedBlockingQueue<>(1000),  //workQueue
                Executors.defaultThreadFactory()
        );
        return Schedulers.fromExecutor(executor);
    }


}
