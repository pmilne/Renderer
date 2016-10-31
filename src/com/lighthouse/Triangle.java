package com.lighthouse;

/**
 * Copyright LighthouseLabs Inc. All rights reserved.
 *
 * @author pmilne
 */
public class Triangle<T> {
    public final T[] vertices;

    public Triangle(T... vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        return "<" + vertices[0] +
                ", " + vertices[1] +
                ", " + vertices[2] +
                '>';
    }
}
