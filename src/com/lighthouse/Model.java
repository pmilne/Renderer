package com.lighthouse;

public class Model {
    public final Vector3[] points;
    public final Triangle<Vector3>[] faces;

    public Model(Vector3[] points, Triangle<Vector3>[] faces) {
        this.points = points;
        this.faces = faces;
    }
}
