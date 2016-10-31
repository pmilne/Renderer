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
