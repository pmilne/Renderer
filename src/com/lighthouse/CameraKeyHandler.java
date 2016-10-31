package com.lighthouse;

import java.util.function.Supplier;

public class CameraKeyHandler extends GenericKeyHandler {

    private final Model model;
    private final Supplier<Camera> cameraHost;

    public CameraKeyHandler(Supplier<Camera> cameraHost, Model model) {
        this.cameraHost = cameraHost;
        this.model = model;
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
            camera.setCentre(Vector3.sum(camera.getCentre(), camera.rotate(step)));
        };
    }

    public static void rotate(Camera camera, Rotation rotation) {
        camera.setRotation(rotation.inverse().compose(camera.getRotation()));
    }

    private Vector3 getTuringPoint() {
//        Vertex modelIntersection = getModelCentre();
//        return modelIntersection != null ? modelIntersection.toVector3() : model.getCentre();
        return model.getRotationCentre();
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
        camera.setCentre(Vector3.sum(modelCentre, Vector3.scale(1 / k, cameraToModel)));
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

    @Override
    protected Action rotate(Vector3 axis) {
        return null;
    }

    @Override
    protected Action crabPan(Vector3 crabAxis, Vector3 panAxis) {
        return super.crabPan(crabAxis, panAxis);
    }
}
