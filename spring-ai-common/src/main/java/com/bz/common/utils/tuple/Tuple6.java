package com.bz.common.utils.tuple;

import com.bz.common.utils.function.Function6;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Tuple6<T1, T2, T3, T4, T5, T6> implements Tuple, Comparable<Tuple6<T1, T2, T3, T4, T5, T6>>, Serializable {

    private static final long serialVersionUID = 1L;

    public final T1 _1;

    public final T2 _2;

    public final T3 _3;

    public final T4 _4;

    public final T5 _5;

    public final T6 _6;

    public Tuple6(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
        this._6 = _6;
    }

    @Override
    public int arity() {
        return 6;
    }

    @Override
    public List<?> toList() {
        return List.of(_1, _2, _3, _4, _5, _6);
    }

    @Override
    public int compareTo(Tuple6<T1, T2, T3, T4, T5, T6> that) {
        return Tuple6.compareTo(this, that);
    }
    
    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>, U3 extends Comparable<? super U3>, U4 extends Comparable<? super U4>, U5 extends Comparable<? super U5>, U6 extends Comparable<? super U6>> int compareTo(Tuple6<?, ?, ?, ?, ?, ?> o1, Tuple6<?, ?, ?, ?, ?, ?> o2) {
        final Tuple6<U1, U2, U3, U4, U5, U6> t1 = (Tuple6<U1, U2, U3, U4, U5, U6>) o1;
        final Tuple6<U1, U2, U3, U4, U5, U6> t2 = (Tuple6<U1, U2, U3, U4, U5, U6>) o2;

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

        final int check4 = t1._4.compareTo(t2._4);
        if (check4 != 0) {
            return check4;
        }

        final int check5 = t1._5.compareTo(t2._5);
        if (check5 != 0) {
            return check5;
        }

        final int check6 = t1._6.compareTo(t2._6);
        if (check6 != 0) {
            return check6;
        }

        // all components are equal
        return 0;
    }
    
    public T1 _1() {
        return _1;
    }

    public Tuple6<T1, T2, T3, T4, T5, T6> update1(T1 value) {
        return new Tuple6<>(value, _2, _3, _4, _5, _6);
    }

    public T2 _2() {
        return _2;
    }

    public Tuple6<T1, T2, T3, T4, T5, T6> update2(T2 value) {
        return new Tuple6<>(_1, value, _3, _4, _5, _6);
    }

    public T3 _3() {
        return _3;
    }

    public Tuple6<T1, T2, T3, T4, T5, T6> update3(T3 value) {
        return new Tuple6<>(_1, _2, value, _4, _5, _6);
    }

    public T4 _4() {
        return _4;
    }

    public Tuple6<T1, T2, T3, T4, T5, T6> update4(T4 value) {
        return new Tuple6<>(_1, _2, _3, value, _5, _6);
    }

    public T5 _5() {
        return _5;
    }

    public Tuple6<T1, T2, T3, T4, T5, T6> update5(T5 value) {
        return new Tuple6<>(_1, _2, _3, _4, value, _6);
    }

    public T6 _6() {
        return _6;
    }

    public Tuple6<T1, T2, T3, T4, T5, T6> update6(T6 value) {
        return new Tuple6<>(_1, _2, _3, _4, _5, value);
    }

    public <U1, U2, U3, U4, U5, U6> Tuple6<U1, U2, U3, U4, U5, U6> map(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, Tuple6<U1, U2, U3, U4, U5, U6>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2, _3, _4, _5, _6);
    }

    public <U1, U2, U3, U4, U5, U6> Tuple6<U1, U2, U3, U4, U5, U6> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2, Function<? super T3, ? extends U3> f3, Function<? super T4, ? extends U4> f4, Function<? super T5, ? extends U5> f5, Function<? super T6, ? extends U6> f6) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        Objects.requireNonNull(f3, "f3 is null");
        Objects.requireNonNull(f4, "f4 is null");
        Objects.requireNonNull(f5, "f5 is null");
        Objects.requireNonNull(f6, "f6 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3), f4.apply(_4), f5.apply(_5), f6.apply(_6));
    }

    public <U> Tuple6<U, T2, T3, T4, T5, T6> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3, _4, _5, _6);
    }

    public <U> Tuple6<T1, U, T3, T4, T5, T6> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3, _4, _5, _6);
    }

    public <U> Tuple6<T1, T2, U, T4, T5, T6> map3(Function<? super T3, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u, _4, _5, _6);
    }

    public <U> Tuple6<T1, T2, T3, U, T5, T6> map4(Function<? super T4, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_4);
        return Tuple.of(_1, _2, _3, u, _5, _6);
    }

    public <U> Tuple6<T1, T2, T3, T4, U, T6> map5(Function<? super T5, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_5);
        return Tuple.of(_1, _2, _3, _4, u, _6);
    }

    public <U> Tuple6<T1, T2, T3, T4, T5, U> map6(Function<? super T6, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_6);
        return Tuple.of(_1, _2, _3, _4, _5, u);
    }

    public <U> U apply(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2, _3, _4, _5, _6);
    }



}
