package com.bz.common.utils.tuple;

import java.io.Serializable;
import java.util.List;

public interface Tuple extends Serializable {

    long serialVersionUID = 1L;

    int MAX_ARITY = 8;

    int arity();

    List<?> toList();

    static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }

}
