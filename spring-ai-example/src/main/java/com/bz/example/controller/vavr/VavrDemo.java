package com.bz.example.controller.vavr;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class VavrDemo {

    public static void main(String[] args) {
        List<String> append = List.of("1").append("2");
        Tuple2<Integer, Integer> integerIntegerTuple2 = Tuple.of(1, 2);
        Option.of(null);
//        Try.of()
    }
}
