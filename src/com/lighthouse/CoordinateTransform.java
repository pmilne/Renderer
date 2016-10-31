package com.lighthouse;

import javax.media.opengl.GL2;

/**
 * Copyright LighthouseLabs Inc. All rights reserved.
 *
 * @author pmilne
 */
public interface CoordinateTransform {
    void transform(GL2 coordinates);
}
