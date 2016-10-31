package com.lighthouse.events;

import com.lighthouse.*;

import java.util.function.Supplier;

import static com.lighthouse.DoubleArrays.mult;
import static com.lighthouse.DoubleArrays.transpose;
import static com.lighthouse.Vector3.sum;

public class CameraKeyHandler extends GenericKeyHandler {
    private final Model model;
    private final Supplier<Camera> cameraHost;
    private Vector3 turningPoint;

    public CameraKeyHandler(Supplier<Camera> cameraHost, Model model) {
        this.cameraHost = cameraHost;
        this.model = model;
        this.turningPoint = Vector3.average(model.points);
        installBindings(bindings);
    }

    private Camera getSelectedCamera() {
        return cameraHost.get();
    }

    @Override
    protected void printState() {
    }

    @Override
    protected Action translate(Vector3 direction) {
        return () -> {
            Camera camera = getSelectedCamera();
            Vector3 step = Vector3.scale(moveIncrement, direction);
            camera.setCentre(sum(camera.getCentre(), camera.rotate(step)));
        };
    }

    public static void rotate(Camera camera, Rotation rotation) {
        camera.setRotation(rotation.compose(camera.getRotation().inverse()).inverse());
    }

    private Vector3 getTuringPoint() {
//        Vertex modelIntersection = getModelCentre();
//        return modelIntersection != null ? modelIntersection.toVector3() : model.getCentre();
        return turningPoint;
    }

    @Override
    protected Action zoom(boolean in) {
        return () -> {
            Camera camera = getSelectedCamera();
            double k = in ? zoomMultiplier : 1 / zoomMultiplier;
            camera.setFocalLength(camera.getFocalLength() * k);
        };
    }

    public static void trackZoom(Camera camera, Vector3 modelCentre, double k) {
        Vector3 cameraToModel = Vector3.diff(camera.getCentre(), modelCentre);
        camera.setFocalLength(camera.getFocalLength() / k);
        camera.setCentre(sum(modelCentre, Vector3.scale(1 / k, cameraToModel)));
    }

    @Override
    protected Action trackZoom(boolean in) {
        return () -> {
            Camera camera = getSelectedCamera();
            Vector3 modelCentre1 = getTuringPoint();
            double k = in ? zoomMultiplier : 1 / zoomMultiplier;
            trackZoom(camera, modelCentre1, k);
        };
    }

    private Vector3 rotateAxis(Vector3 axis) {
        return getSelectedCamera().rotate(axis);
    }

    private Rotation getEulerRotation(Vector3 axis) {
        return Rotation.create(turnIncrement, axis);
    }

    @Override
    protected Action rotate(Vector3 axis) {
        return () -> {
            Camera camera = getSelectedCamera();
            Rotation eulerRotation = getEulerRotation(rotateAxis(axis));
            rotate(camera, eulerRotation);
        };
    }

    private static void crabPan(Camera camera, Vector3 pivot, Rotation rotation) {
        double[][] R = rotation.toArray3x3();
        double[] cameraCentreRelativeToModelCentre = Vector3.diff(camera.getCentre(), pivot).toArray();
        Vector3 rotated = new Vector3(mult(transpose(R), cameraCentreRelativeToModelCentre));
        camera.setCentre(sum(rotated, pivot));
        camera.setRotation(new Rotation(mult(camera.getRotation().inverse().toArray3x3(), R)).inverse());
    }

    @Override
    protected Action crabPan(Vector3 crabAxis, Vector3 panAxis) {
        return () -> {
            Camera camera = getSelectedCamera();
            Vector3 modelCentre = getTuringPoint();
            Rotation rotation = getEulerRotation(rotateAxis(panAxis));
            crabPan(camera, modelCentre, rotation);
        };
    }
}
