package com.lighthouse;

import java.io.*;
import java.util.Arrays;

public class PlyFileReader {
    private static double[] parseDoubles(String s) {
        return Arrays.stream(s.split("\\s+")).mapToDouble(Double::parseDouble).toArray();
    }

    private static int[] parseInts(String s) {
        return Arrays.stream(s.split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }

    public Model readFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        reader.readLine(); // ply
        reader.readLine(); // format ascii 1.0
        String vertexMetaData = reader.readLine(); // element vertex 3980
        int vertexCount = Integer.parseInt(vertexMetaData.split("\\s+")[2]);
        System.out.println("Point count: " + vertexCount);
        reader.readLine(); // property float x
        reader.readLine(); // property float y
        reader.readLine(); // property float z
        String faceMetaData = reader.readLine(); // element face 2336
        int faceCount = Integer.parseInt(faceMetaData.split("\\s+")[2]);
        System.out.println("Face count: " + faceCount);
        reader.readLine(); // property list uchar int vertex_indices
        reader.readLine(); // end_header
        Vector3[] points = new Vector3[vertexCount];
        for (int i = 0; i < points.length; i++) {
            double[] doubles = parseDoubles(reader.readLine()); // 2.062437 1.730382 -7.850948
            points[i] = new Vector3(doubles);
        }
        Triangle<Vector3>[] faces = new Triangle[faceCount];
        for (int i = 0; i < faces.length; i++) {
            int[] ints = parseInts(reader.readLine()); // 3 605 591 618
            assert ints[0] == 3;
            faces[i] = new Triangle<>(points[ints[1]], points[ints[2]], points[ints[3]]);
        }
        // Close the input stream
        reader.close();

        return new Model(points, faces);
    }

    public static void main(String[] args) throws IOException {
        Model model = new PlyFileReader().readFile(new File("models/Globe/model.ply"));
        System.out.println("keys = " + model);
    }
}
