package org.opencompare.api.java.util;

/**
 * Created by gbecan on 03/02/15.
 */
public class Pair<A, B> {

    public Pair() {

    }

    public Pair(A _1, B _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public A _1;
    public B _2;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Pair) {
            Pair pair = (Pair) obj;
            if (pair._1.equals(_1) && pair._2.equals(_2)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return _1.hashCode() + _2.hashCode();
    }

    @Override
    public String toString() {
        return "[" + _1 + ", " + _2 + ']';
    }
}
