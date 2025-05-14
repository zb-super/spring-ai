package com.bz.common.utils.tuple;

import com.bz.common.utils.function.Function3;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Tuple3<T1, T2, T3> implements Tuple, Comparable<Tuple3<T1, T2, T3>>, Serializable {

    private static final long serialVersionUID = 1L;

    public final T1 _1;

    public final T2 _2;

    public final T3 _3;

    public Tuple3(T1 t1, T2 t2, T3 t3) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
    }

    @Override
    public int arity() {
        return 3;
    }

    @Override
    public List<?> toList() {
        return List.of(_1, _2, _3);
    }

    @Override
    public int compareTo(Tuple3<T1, T2, T3> that) {
        return Tuple3.compareTo(this, that);
    }

    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>, U3 extends Comparable<? super U3>> int compareTo(Tuple3<?, ?, ?> o1, Tuple3<?, ?, ?> o2) {
        final Tuple3<U1, U2, U3> t1 = (Tuple3<U1, U2, U3>) o1;
        final Tuple3<U1, U2, U3> t2 = (Tuple3<U1, U2, U3>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        final int check2 = t1._2.compareTo(t2._2);
        if (check2 != 0) {
            return check2;
        }

        final int check3 = t1._3.compareTo(t2._3);
        if (check3 != 0) {
            return check3;
        }

        // all components are equal
        return 0;
    }

    public T1 _1() {
        return _1;
    }

    public Tuple3<T1, T2, T3> update1(T1 value) {
        return new Tuple3<>(value, _2, _3);
    }

    public T2 _2() {
        return _2;
    }

    public Tuple3<T1, T2, T3> update2(T2 value) {
        return new Tuple3<>(_1, value, _3);
    }

    public T3 _3() {
        return _3;
    }

    public Tuple3<T1, T2, T3> update3(T3 value) {
        return new Tuple3<>(_1, _2, value);
    }

    public <U1, U2, U3> Tuple3<U1, U2, U3> map(Function3<? super T1, ? super T2, ? super T3, Tuple3<U1, U2, U3>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2, _3);
    }

    public <U1, U2, U3> Tuple3<U1, U2, U3> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2, Function<? super T3, ? extends U3> f3) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        Objects.requireNonNull(f3, "f3 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3));
    }

    public <U> Tuple3<U, T2, T3> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3);
    }

    public <U> Tuple3<T1, U, T3> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3);
    }

    public <U> Tuple3<T1, T2, U> map3(Function<? super T3, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u);
    }

    public <U> U apply(Function3<? super T1, ? super T2, ? super T3, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2, _3);
    }



}
