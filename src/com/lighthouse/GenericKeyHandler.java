package com.lighthouse;

import java.util.Map;

import static java.awt.event.KeyEvent.*;

/**
 * Copyright Mindsilver Inc. All rights reserved.
 *
 * @author pmilne
 */
public abstract class GenericKeyHandler extends AbstractKeyHandler {
    private static final Vector3 X = new Vector3(1, 0, 0);
    private static final Vector3 Y = new Vector3(0, 1, 0);
    private static final Vector3 Z = new Vector3(0, 0, 1);

    protected static final Vector3 P_X = Vector3.scale(+1, X);
    protected static final Vector3 P_Y = Vector3.scale(+1, Y);
    protected static final Vector3 P_Z = Vector3.scale(+1, Z);

    protected static final Vector3 M_X = Vector3.scale(-1, X);
    protected static final Vector3 M_Y = Vector3.scale(-1, Y);
    protected static final Vector3 M_Z = Vector3.scale(-1, Z);
    public static final Action ACTION_NOT_IMPLEMENTED = () -> {
        throw new UnsupportedOperationException();
    };

    //    private double moveIncrement = 0.002;
//    private double turnIncrement = 0.1; // degrees
    protected double turnIncrement = 4; // degrees
    protected double moveIncrement = Math.toRadians(turnIncrement); // this assumes subject is one unit away
    protected double zoomMultiplier = 1.01;

    protected void installBindings(Map<KeyBinding, Action> result) {
        // Translation
        result.put(new KeyBinding(0, VK_LEFT), translate(M_X));
        result.put(new KeyBinding(0, VK_RIGHT), translate(P_X));
        result.put(new KeyBinding(0, VK_UP), translate(P_Y));
        result.put(new KeyBinding(0, VK_DOWN), translate(M_Y));
        // Up and down are reversed for the z-axis as the positive z direction runs out of the screen, towards us.
        // This direction, is more naturally bound to the 'down' key (which also points towards us on the keyboard)
        // even though mathematically it raises the value of z.
        result.put(new KeyBinding(CTRL_MASK, VK_UP), translate(M_Z));
        result.put(new KeyBinding(CTRL_MASK, VK_DOWN), translate(P_Z));
        result.put(new KeyBinding(CTRL_MASK | SHIFT_MASK, VK_UP), zoom(true));
        result.put(new KeyBinding(CTRL_MASK | SHIFT_MASK, VK_DOWN), zoom(false));
        result.put(new KeyBinding(CTRL_MASK | ALT_MASK, VK_UP), trackZoom(true));
        result.put(new KeyBinding(CTRL_MASK | ALT_MASK, VK_DOWN), trackZoom(false));

        // Rotation
        result.put(new KeyBinding(SHIFT_MASK, VK_LEFT), rotate(M_Y));
        result.put(new KeyBinding(SHIFT_MASK, VK_RIGHT), rotate(P_Y));
        result.put(new KeyBinding(SHIFT_MASK, VK_UP), rotate(M_X));
        result.put(new KeyBinding(SHIFT_MASK, VK_DOWN), rotate(P_X));
        result.put(new KeyBinding(CTRL_MASK, VK_LEFT), rotate(M_Z));
        result.put(new KeyBinding(CTRL_MASK, VK_RIGHT), rotate(P_Z));

        // CrabPan
        result.put(new KeyBinding(ALT_MASK, VK_LEFT), crabPan(P_Y, M_Y));
        result.put(new KeyBinding(ALT_MASK, VK_RIGHT), crabPan(M_Y, P_Y));
        result.put(new KeyBinding(ALT_MASK, VK_UP), crabPan(M_X, P_X));
        result.put(new KeyBinding(ALT_MASK, VK_DOWN), crabPan(P_X, M_X));

        // Misc
        result.put(new KeyBinding(SHIFT_MASK, VK_EQUALS), accelerate(2)); // plus
        result.put(new KeyBinding(0, VK_MINUS), accelerate(0.5));

        result.put(new KeyBinding(META_MASK, VK_P), () -> printState());
    }

    private Action accelerate(double k) {
        return () -> {
            moveIncrement *= k;
            turnIncrement *= k;
            zoomMultiplier = Math.pow(zoomMultiplier, k);
        };
    }

    protected abstract void printState();

    protected abstract Action translate(Vector3 direction);

    protected abstract Action rotate(Vector3 axis);

    protected Action crabPan(Vector3 crabAxis, Vector3 panAxis) {
        return ACTION_NOT_IMPLEMENTED;
    }

    protected Action zoom(boolean in) {
        return ACTION_NOT_IMPLEMENTED;
    }

    protected Action trackZoom(boolean in) {
        return ACTION_NOT_IMPLEMENTED;
    }
}
