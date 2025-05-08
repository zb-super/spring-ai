package com.zb.reactor.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.util.function.Consumer;

public class GenerateAndCreateDemo {



    public static void main(String[] args) {
//        Flux.generate(() -> 0, (state,sink) ->{
//            if (state == 10){
//                sink.complete();
//            }
//            sink.next(state);
//            return state + 1;
//        }).subscribe(System.out::println);

        MultiCast multiCast  = new MultiCast();
        Flux.create(new Consumer<FluxSink<? extends Object>>() {
            @Override
            public void accept(FluxSink<?> fluxSink) {
                System.out.println(1);
            }
        }).subscribe(System.out::println);
        for (int i = 0; i< 20; i++){
            Thread thread = new Thread(() -> {
                multiCast.print();;
            });
            thread.start();
        }
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private static class MultiCast {
        private FluxSink<Object> fluxSink;
        public FluxSink<Object> getFluxSink() {
            return fluxSink;
        }
        public void setFluxSink(FluxSink<Object> fluxSink) {
            this.fluxSink = fluxSink;
        }

        public void print(){
            fluxSink.next(1);
        }
    }
}
