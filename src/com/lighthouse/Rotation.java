package com.lighthouse;

import java.util.Arrays;

import static com.lighthouse.DoubleArrays.mult;

/**
 * Copyright LighthouseLabs Inc. All rights reserved.
 *
 * @author pmilne
 */
public class Rotation {
    public static final Rotation IDENTITY = new Rotation(DoubleArrays.identity(3));
    private static final int[][] SIGNS = new int[][]{{0, -1, 1}, {1, 0, -1}, {-1, 1, 0}}; // (1 + (i - j) mod 3) - 1; // use Math.floorMod
    private static final int[][] INDEXES = new int[][]{{-1, 2, 1}, {2, -1, 0}, {1, 0, -1}}; // (- (i + j)) mod 3 where (i != j)
    public static double UNIT = 360;
    public static double K = (2 * Math.PI) / UNIT;

    private final double[][] matrix;

    public Rotation(double[][] matrix) {
        this.matrix = matrix;
    }

    public Vector3 rotate(Vector3 vector) {
        return new Vector3(DoubleArrays.mult(toArray3x3(), vector.toArray()));
    }

    public Rotation inverse() {
        return new Rotation(DoubleArrays.transpose(matrix));
    }

    public Rotation compose(Rotation rotation) {
        return new Rotation(DoubleArrays.mult(rotation.toArray3x3(), toArray3x3()));
    }

    public double[][] toArray3x3() {
        return DoubleArrays.copy(matrix);
    }

    // For the purposes of supplying to gl's matrix multiply function.
    public double[][] toArray4x4() {
        double[][] result = new double[4][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        result[3][3] = 1;
        return result;
    }

    // from: http://en.wikipedia.org/wiki/Rotation_formalisms_in_three_dimensions
    public static Rotation create(double angle, Vector3 axis) {
        if (angle == 0) {
            return IDENTITY;
        }
        double cos = Math.cos(angle * K);
        double sin = Math.sin(angle * K);

        double[][] A = new double[3][3];

        double[] axisArray = axis.toArray();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double t1 = (i == j) ? cos : 0;
                double t2 = (1 - cos) * axisArray[i] * axisArray[j];
                double t3 = (i != j) ? SIGNS[i][j] * axisArray[INDEXES[i][j]] * sin : 0;
                A[i][j] = t1 + t2 + t3;
            }
        }

        return new Rotation(A);
    }

    public double[] toArray9() {
        return DoubleArrays.flatten(toArray3x3());
    }

    public double[] toArray16() {
        return DoubleArrays.flatten(toArray4x4());
    }


    
    @Override
    public String toString() {
        return "Rotation<" + Arrays.deepToString(matrix) + '>';
    }
}
