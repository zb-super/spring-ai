package com.bz.common.utils.function;

import java.io.Serializable;

@FunctionalInterface
public interface Function2<T1, T2, R> extends Serializable {

    long serialVersionUID = 1L;

    R apply(T1 t1, T2 t2);

}
