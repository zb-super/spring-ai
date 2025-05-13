package com.bz.reactor.demo;

import reactor.core.publisher.Flux;

public class HandleDemo {


    public static void main(String[] args) {
        Flux.range(1, 10)
                .handle((val, sink) -> sink.next(val + "------------->" + val))
                .log()
                .subscribe();
    }
}
