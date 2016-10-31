package com.lighthouse;

import java.util.Arrays;
import java.util.List;

public class Triangle<T> {
    public final T a;
    public final T b;
    public final T c;

    public Triangle(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public List<T> getVertices() {
        return Arrays.asList(a, b, c);
    }

    @Override
    public String toString() {
        return "<" + a +
                ", " + b +
                ", " + c +
                '>';
    }
}
