package com.lighthouse;

import com.lighthouse.events.AbstractKeyHandler;
import com.lighthouse.events.CameraKeyHandler;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import static javax.media.opengl.GL2.*;

public class Renderer {
    private static double getK(Triangle<Vector3> face) {
        Vector3 A = Vector3.diff(face.b, face.a);
        Vector3 B = Vector3.diff(face.c, face.b);
        Vector3 C = Vector3.diff(face.a, face.c);
        double d0 = Vector3.dot(C, A);
        double d1 = Vector3.dot(B, A);
        return d0 / (d0 + d1);
    }

    private static GLEventListener createGLEventListener(Camera screen, Model model, File imagesDirectory) {
        List<Triangle<Vector3>> faces = Arrays.asList(model.faces);
        int size = faces.size();

        return new GLEventListener() {
            private Texture[] textures = new Texture[size];

            @Override
            public void init(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                for (int i = 0; i < size; i++) {
                    File imageFile = new File(imagesDirectory, i + ".png");
                    textures[i] = new Texture(gl, imageFile);
                }
            }

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                screen.setSize(new Dimension(width, height));
            }

            @Override
            public void display(GLAutoDrawable drawable) {
                Graphics g = Graphics.get(drawable.getGL().getGL2());
                screen.bind(g)
                        .with(GL_DEPTH_TEST, true)
                        .with(GL_CULL_FACE, true)
                        .with(GL_TEXTURE_2D, true)
                        .draw(gl -> {
                            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                            for (int i = 0; i < size; i++) {
                                Triangle<Vector3> face = faces.get(i);
                                double k = getK(face);
                                List<Vector3> vertices = face.getVertices();
                                Graphics.get(gl)
                                        .with(textures[i])
                                        .draw(gl2 -> {
                                            gl2.glBegin(GL_TRIANGLES);
                                            for (int j = 0; j < vertices.size(); j++) {
                                                Vector3 v = vertices.get(j);
                                                Vector2 tc = j == 0 ? new Vector2(0, 0) : j == 1 ? new Vector2(1, 0) : new Vector2(k, 1);
                                                gl2.glTexCoord2d(tc.x, tc.y); // has to be done before glVertex() call
                                                gl2.glVertex3d(v.x, v.y, v.z);
                                            }
                                            gl2.glEnd();
                                        });
                            }
                        });
            }

            @Override
            public void dispose(GLAutoDrawable drawable) {
            }
        };
    }

    private static Camera getScreenCamera() {
        Camera camera = new Camera();
        camera.setCentre(new Vector3(0, 0, 5));
        camera.setFocalLength(2000);
        return camera;
    }

    public static void createRendererUI(File directory, Model model, Dimension canvasSize) {
        JFrame frame = new JFrame();
        frame.setTitle(Renderer.class.getSimpleName() + ": " + directory.getAbsolutePath());
        GLCanvas canvas = new GLCanvas();
        Camera camera = getScreenCamera();

        // Add listeners
        canvas.addGLEventListener(createGLEventListener(camera, model, new File(directory, "textures")));
        CameraKeyHandler cameraKeyHandler = new CameraKeyHandler(() -> camera, model);
        canvas.addKeyListener(new AbstractKeyHandler() {
            @Override
            public void keyPressed(KeyEvent e) {
                cameraKeyHandler.keyPressed(e);
                canvas.repaint();
            }
        });

        canvas.setPreferredSize(canvasSize);
        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setLocation(500, 0);
        frame.setVisible(true);
        canvas.requestFocus();
    }

    public static void main(String... args) throws Exception {
        File directory = new File("models/Globe");
        if (directory.exists()) {
            Model model = new PlyFileReader().readFile(new File(directory, "model.ply"));
            createRendererUI(directory, model, new Dimension(1920 / 4, 1080 / 4));
        }
    }

}
