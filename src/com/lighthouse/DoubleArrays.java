package com.lighthouse;

import java.util.Arrays;

public class DoubleArrays {
    public static void checkSquare(double[][] a) {
        int N = a.length;
        for (double[] row : a) {
            assert row.length == N;
        }
    }

    public static double[][] transpose(double[][] a) {
        checkSquare(a);
        int N = a.length;
        double[][] result = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result[i][j] = a[j][i];
            }
        }
        return result;
    }

    public static double dot(double[] a, double[] b) {
        int N = a.length;
        double result = 0;
        for (int i = 0; i < N; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    public static double[] mult(double[][] a, double[] v) {
        checkSquare(a);
        int N = a.length;
        double[] result = new double[N];
        for (int i = 0; i < N; i++) {
            result[i] = dot(a[i], v);
        }
        return result;
    }

    public static double[][] mult(double[][] a, double[][] b) {
        checkSquare(a);
        checkSquare(b);
        int N = a.length;
        double[][] bt = transpose(b);

        double[][] result = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result[i][j] = dot(a[i], bt[j]);
            }
        }
        return result;
    }

    public static double[][] copy(double[][] a) {
        int N = a.length;
        double[][] result = new double[N][];
        for (int i = 0; i < N; i++) {
            result[i] = a[i].clone();
        }
        return result;
    }

    public static double[][] identity(int N) {
        double[][] result = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result[i][j] = (i == j) ? 1 : 0;
            }
        }
        return result;
    }

    public static double[] flatten(double[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        double[] result = new double[rows * cols];
        int k = 0;
        for (int i = 0; i < rows; i++) {
            double[] row = mat[i];
            assert row.length == cols;
            for (int j = 0; j < cols; j++) {
                result[k++] = row[j];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        double[][] a = {{1, 2}, {3, 4}};
        double[][] b = {{1, 3}, {5, 7}};
        double[][] m = mult(a, b);
        System.out.println("m = " + Arrays.deepToString(m));
    }
}
