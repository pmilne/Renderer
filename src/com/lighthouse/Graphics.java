package com.lighthouse;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MATRIX_MODE;

public abstract class Graphics {

    // Static utilities

    private static void setEnabled(GL2 gl, int cap, boolean value) {
        if (value) {
            gl.glEnable(cap);
        } else {
            gl.glDisable(cap);
        }
    }

    public static int getIntValue(GL2 gl, int pName) {
        int[] results = new int[1];
        gl.glGetIntegerv(pName, results, 0);
        return results[0];
    }

    private static double getDoubleValue(GL2 gl, int pName) {
        double[] results = new double[1];
        gl.glGetDoublev(pName, results, 0);
        return results[0];
    }

    // Implementation

    public static Graphics get(GL2 gl) {
        return new Graphics() {
            @Override
            public void draw(Drawable d) {
                d.draw(gl); // the base graphics object just draws directly using the unmodified gl context.
            }
        };
    }

    public abstract void draw(Drawable d);

    public Graphics with(int cap, boolean value) {
        return new Graphics() {
            @Override
            public void draw(Drawable d) {
                Graphics.this.draw(gl -> {
                    boolean oldValue = gl.glIsEnabled(cap);
                    setEnabled(gl, cap, value);
                    d.draw(gl);
                    setEnabled(gl, cap, oldValue);
                });
            }
        };
    }

    public Graphics withClearDepth(double depth) {
        return new Graphics() {
            @Override
            public void draw(Drawable d) {
                Graphics.this.draw(gl -> {
                    double oldValue = getDoubleValue(gl, GL_DEPTH_CLEAR_VALUE);
                    gl.glClearDepth(depth);
                    d.draw(gl);
                    gl.glClearDepth(oldValue);
                });
            }
        };
    }

    public Graphics withDepthFunction(int depthFunction) {
        return new Graphics() {
            @Override
            public void draw(Drawable d) {
                Graphics.this.draw(gl -> {
                    int old = getIntValue(gl, GL_DEPTH_FUNC);
                    gl.glDepthFunc(depthFunction);
                    d.draw(gl);
                    gl.glDepthFunc(old);
                });
            }
        };
    }

    public Graphics withTransform(int matrixMode, CoordinateTransform transform) {
        return new Graphics() {
            @Override
            public void draw(Drawable d) {
                Graphics.this.draw(gl -> {
                    int previousMatrixMode = getIntValue(gl, GL_MATRIX_MODE);
                    gl.glMatrixMode(matrixMode);
                    gl.glPushMatrix();
                    gl.glLoadIdentity();
                    transform.transform(gl);
                    d.draw(gl);
                    gl.glPopMatrix();
                    gl.glMatrixMode(previousMatrixMode);
                });
            }
        };
    }
}
