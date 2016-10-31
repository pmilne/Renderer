package com.lighthouse.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractKeyHandler extends KeyAdapter {
    protected Map<KeyBinding, Action> bindings  = new HashMap<>();
    
    public static interface Action {
        public abstract void perform();
    }

        @Override
    public void keyPressed(KeyEvent e) {
        Action action = bindings.get(new KeyBinding(e.getModifiers(), e.getKeyCode()));
        if (action != null) {
            action.perform();
        }
    }

    public static class KeyBinding {
        public final int modifier;
        public final int keyCode;
    
        public KeyBinding(int modifier, int keyCode) {
            this.modifier = modifier;
            this.keyCode = keyCode;
        }
    
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
    
            KeyBinding that = (KeyBinding) o;
    
            if (keyCode != that.keyCode) {
                return false;
            }
            if (modifier != that.modifier) {
                return false;
            }
    
            return true;
        }
    
        @Override
        public int hashCode() {
            int result = modifier;
            result = 31 * result + keyCode;
            return result;
        }
    }
}
