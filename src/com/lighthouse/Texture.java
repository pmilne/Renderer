package com.lighthouse;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.TextureIO;

import static javax.media.opengl.GL2.*;

public class Texture {
    private static final float[] TRANSPARENT = new float[]{0, 0, 0, 0};

    private int id;

    private static com.jogamp.opengl.util.texture.Texture getTexture2(GL2 gl, File file) {
        try {
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            com.jogamp.opengl.util.texture.Texture texture = TextureIO.newTexture(new FileInputStream(file), false, extension);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

            gl.glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, TRANSPARENT, 0);
            return texture;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Texture(GL2 gl, com.jogamp.opengl.util.texture.Texture texture) {
        this.id = texture.getTextureObject(gl);
    }

    public Texture(GL2 gl, File file) {
        this(gl, getTexture2(gl, file));
    }

    public Graphics bind(Graphics graphics) {
        return new Graphics() {
            @Override
            public void draw(Drawable d) {
                graphics.draw(gl -> {
                    int oldTexture = Graphics.getIntValue(gl, GL_TEXTURE_BINDING_2D);
                    gl.glBindTexture(GL_TEXTURE_2D, id);
                    d.draw(gl);
                    gl.glBindTexture(GL_TEXTURE_2D, oldTexture);
                });
            }
        };
    }
}
