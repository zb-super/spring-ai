package com.zb.reactor.demo;

import reactor.core.publisher.Flux;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/7
 */
public class BufferDemo {
    public static void main(String[] args) {
        Flux.just(1,2,3,4,5,6,7)
                .buffer(2)
                .log()
                .subscribe();
    }

}
