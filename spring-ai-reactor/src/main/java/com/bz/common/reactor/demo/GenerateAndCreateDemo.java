package com.bz.common.reactor.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Consumer;

public class GenerateAndCreateDemo {


    public static void main(String[] args) throws IOException {
//        Flux.generate(() -> 0, (state,sink) ->{
//            if (state == 10){
//                sink.complete();
//            }
//            sink.next(state);
//            return state + 1;
//        }).subscribe(System.out::println);

        Flux.create(new Consumer<FluxSink<Integer>>() {
            @Override
            public void accept(FluxSink<Integer> fluxSink) {
                Flux.range(1,10)
                        .delayElements(Duration.ofSeconds(5))
                        .doOnNext(item -> fluxSink.next(item))
                        .collectList()
                        .block();

            }
        }).subscribe(System.out::println);

        System.in.read();

    }
}
