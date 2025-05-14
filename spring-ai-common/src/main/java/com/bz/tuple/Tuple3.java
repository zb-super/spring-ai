package com.bz.tuple;

import java.io.Serializable;
import java.util.List;

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


}
