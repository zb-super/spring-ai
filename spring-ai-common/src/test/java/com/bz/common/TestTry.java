package com.bz.common;

import com.bz.common.utils.exception.Try;

public class TestTry {
    public static void main(String[] args) {
        Try.of(() -> 1/0)
                .onFailure(throwable -> {
                    // dou some thing
                })
                .onSuccess(val -> {
                    // dou some thing
                })
                .andFinally(() -> {
                    System.out.println("finally");
                }).get();
    }
}
