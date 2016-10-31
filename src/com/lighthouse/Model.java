package com.lighthouse;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Model {
    private Vector3 centre;
    public Vector3[] points;
    public Triangle<Vector3>[] faces;

    public Model(Vector3[] points, Triangle<Vector3>[] faces) {
        this.centre = Vector3.average(points);
        this.points = points;
        this.faces = faces;
    }

    public Vector3 getRotationCentre() {
        return centre;
    }

    public List<Triangle<Vector3>> getTriangles() {
        return Arrays.asList(faces);
    }

    public static Model getModel(File file) throws IOException {
        return new PlyFileReader().readFile(file);
    }
}
