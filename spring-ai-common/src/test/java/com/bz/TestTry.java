package com.bz;

import com.bz.exception.Try;

public class TestTry {
    public static void main(String[] args) {
        Try.of(() -> 1/0)
                .onFailure(throwable -> {

                })
                .onSuccess(val -> {

                })
                .andFinally(() -> System.out.println("c出错了"))
                .get();


    }
}
