package com.lighthouse;

public class Vector3 {
    public final double x;
    public final double y;
    public final double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(double[] a) {
        this(a[0], a[1], a[2]);
        assert a.length == 3;
    }

    public double[] toArray() {
        return new double[]{x, y, z};
    }

    // Static methods

    public static Vector3 sum(Vector3... vertices) {
        double x = 0;
        double y = 0;
        double z = 0;
        for (Vector3 v : vertices) {
            x += v.x;
            y += v.y;
            z += v.z;
        }
        return new Vector3(x, y, z);
    }

    public static Vector3 average(Vector3... vertices) {
        return scale(1.0 / vertices.length, sum(vertices));
    }

    public static Vector3 diff(Vector3 a, Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static double dot(Vector3 a, Vector3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector3 scale(double k, Vector3 v) {
        return new Vector3(k * v.x, k * v.y, k * v.z);
    }

    private static double sq(double a) {
        return a * a;
    }

    public static double length2(Vector3 v) {
        return sq(v.x) + sq(v.y) + sq(v.z);
    }

    public static double length(Vector3 v) {
        return Math.sqrt(length2(v));
    }

    public static double distance(Vector3 p, Vector3 q) {
        return length(diff(p, q));
    }

}
