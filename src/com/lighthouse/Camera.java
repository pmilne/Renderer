package com.lighthouse;

import java.awt.Dimension;

import static javax.media.opengl.GL2.*;

public final class Camera {
    private static final double CLEAR_DEPTH = 0;
    private static final int DEPTH_TEST = GL_GEQUAL;
    private static final double[] PROJECTION_TRANSFORM = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 0, -1,
            0, 0, 1. / 65536, 0
    };

    private Dimension size;
    private Vector3 centre = new Vector3(0, 0, 0);
    private Rotation rotation = Rotation.IDENTITY;
    private double focalLength = 1000;

    public Vector3 getCentre() {
        return centre;
    }

    public void setCentre(Vector3 centre) {
        this.centre = centre;
    }

    public double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(double focalLength) {
        this.focalLength = focalLength;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public CoordinateTransform getProjectionTransform() {
        return gl -> {
            gl.glMultMatrixd(PROJECTION_TRANSFORM, 0); // the 'last' (division) step of the projection comes first
            if (focalLength == 0) {
                System.err.println("The screen camera has a focal length of zero.");
            } else {
                Vector3 scale = new Vector3(size.width / 2.0, size.height / 2.0, focalLength);
                gl.glScaled(1 / scale.x, 1 / scale.y, 1 / scale.z);
            }
        };
    }

    public CoordinateTransform getModelViewTransform() {
        return gl -> {
            // we are using rowMajorOrder internally. GL uses columnMajorOrder which gives us the inverse rotation.
            gl.glMultMatrixd(rotation.toArray16(), 0);
            gl.glTranslated(-centre.x, -centre.y, -centre.z); // apply the inverse translation
        };
    }

    public Graphics bind(Graphics g) {
        return g.withClearDepth(CLEAR_DEPTH)
                .withDepthFunction(DEPTH_TEST)
                .withTransform(GL_MODELVIEW, getModelViewTransform())
                .withTransform(GL_PROJECTION, getProjectionTransform());
    }

    public Vector3 rotate(Vector3 v) {
        return rotation.rotate(new Vector3(v.toArray()));
    }
}
