package com.zb.reactor;

import reactor.core.publisher.Flux;

public class Main {

    public static void main(String[] args) {
        Flux.range(1,10)
                .subscribe(System.out::println);
    }
}
