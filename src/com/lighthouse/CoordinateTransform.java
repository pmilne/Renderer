package com.lighthouse;

import javax.media.opengl.GL2;

public interface CoordinateTransform {
    void transform(GL2 coordinates);
}
