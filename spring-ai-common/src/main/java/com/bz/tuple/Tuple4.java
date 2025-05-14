package com.bz.tuple;

import java.io.Serializable;
import java.util.List;

public class Tuple4<T1, T2, T3, T4> implements Tuple, Comparable<Tuple4<T1, T2, T3, T4>>, Serializable {

    private static final long serialVersionUID = 1L;

    public final T1 _1;

    public final T2 _2;

    public final T3 _3;

    public final T4 _4;

    public Tuple4(T1 _1, T2 _2, T3 _3, T4 _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }

    @Override
    public int arity() {
        return 4;
    }

    @Override
    public List<?> toList() {
        return List.of(_1, _2, _3, _4);
    }

    @Override
    public int compareTo(Tuple4<T1, T2, T3, T4> that) {
        return 0;
    }
}
