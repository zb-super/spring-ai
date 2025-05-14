package com.bz.common.utils.tuple;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Tuple2<T1, T2> implements Tuple, Comparable<Tuple2<T1, T2>>, Serializable {

    private static final long serialVersionUID = 1L;

    public final T1 _1;

    public final T2 _2;

    public Tuple2(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public List<?> toList() {
        return List.of(_1, _2);
    }

    @Override
    public int compareTo(Tuple2<T1, T2> that) {
        return Tuple2.compareTo(this, that);
    }

    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>> int compareTo(Tuple2<?, ?> o1, Tuple2<?, ?> o2) {
        final Tuple2<U1, U2> t1 = (Tuple2<U1, U2>) o1;
        final Tuple2<U1, U2> t2 = (Tuple2<U1, U2>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        final int check2 = t1._2.compareTo(t2._2);
        if (check2 != 0) {
            return check2;
        }

        // all components are equal
        return 0;
    }

    public T1 _1() {
        return _1;
    }

    public Tuple2<T1, T2> update1(T1 value) {
        return new Tuple2<>(value, _2);
    }

    public T2 _2() {
        return _2;
    }

    public Tuple2<T1, T2> update2(T2 value) {
        return new Tuple2<>(_1, value);
    }

    public Tuple2<T2, T1> swap() {
        return Tuple.of(_2, _1);
    }

    public Map.Entry<T1, T2> toEntry() {
        return new AbstractMap.SimpleEntry<>(_1, _2);
    }

    public <U1, U2> Tuple2<U1, U2> map(BiFunction<? super T1, ? super T2, Tuple2<U1, U2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2);
    }

    public <U1, U2> Tuple2<U1, U2> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2));
    }

    public <U> Tuple2<U, T2> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2);
    }

    public <U> Tuple2<T1, U> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u);
    }

    public <U> U apply(BiFunction<? super T1, ? super T2, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2);
    }

    public <T3> Tuple3<T1, T2, T3> append(T3 t3) {
        return Tuple.of(_1, _2, t3);
    }

}
