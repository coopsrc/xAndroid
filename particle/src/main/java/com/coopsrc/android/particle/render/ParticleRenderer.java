package com.coopsrc.android.particle.render;

import android.graphics.Bitmap;
import android.opengl.GLES31;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.coopsrc.android.particle.Particle;
import com.coopsrc.android.particle.R;
import com.coopsrc.android.particle.texture.Region;
import com.coopsrc.android.particle.texture.TextureAtlas;
import com.coopsrc.android.particle.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class ParticleRenderer extends BaseParticleRenderer {

    private static final double NANOSECONDS = TimeUnit.SECONDS.toNanos(1);

    private int surfaceWidth;
    private int surfaceHeight;

    private int program;

    private float[] projectionViewM = new float[16];

    private ShortBuffer drawListBuffer;

    private float[] vertexArray;
    private FloatBuffer vertexBuffer;

    private float[] alphaArray;
    private FloatBuffer alphaBuffer;

    private float[] textureCoordsCacheArray;
    private float[] textureCoordsArray;
    private FloatBuffer textureCoordsBuffer;

    private long lastUpdateTime;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initGl();
        initGlProgram();
        particleSystemNeedsSetup = true;
        textureAtlasNeedsSetup = true;
        lastUpdateTime = 0;
    }

    private void initGl() {
        GLES31.glClearColor(0f, 0f, 0f, 0f);
        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void initGlProgram() {
        int vertexShader = ShaderUtils.compileVertexShader(R.raw.particle_vertex_shader);
        int fragmentShader = ShaderUtils.compileFragmentShader(R.raw.particle_fragment_shader);
        program = ShaderUtils.linkProgram(vertexShader, fragmentShader);
        GLES31.glUseProgram(program);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        surfaceWidth = width;
        surfaceHeight = height;
        initProjectionViewMatrix(width, height);
    }

    private void initProjectionViewMatrix(int width, int height) {
        float[] projectionM = new float[16];
        float[] viewM = new float[16];
        Matrix.orthoM(projectionM, 0, 0f, width, 0f, height, 0, 1f);
        Matrix.setLookAtM(viewM, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f);
        Matrix.multiplyMM(projectionViewM, 0, projectionM, 0, viewM, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        if (particleSystem == null) {
            return;
        }
        if (particleSystemNeedsSetup) {
            setupBuffers();
            particleSystemNeedsSetup = false;
        }
        if (textureAtlasNeedsSetup) {
            TextureAtlas textureAtlas = textureAtlasFactory.createTextureAtlas();
            textureAtlas.setEditable(false);
            setupTextures(textureAtlas);
            textureAtlasNeedsSetup = false;
        }
        long updateTime = System.nanoTime();
        if (lastUpdateTime == 0) {
            lastUpdateTime = updateTime;
        }
        List<? extends Particle> particles = particleSystem.update((updateTime - lastUpdateTime) / NANOSECONDS);
        lastUpdateTime = updateTime;
        updateBuffers(particles);
        render(particles.size());
    }

    private void setupTextures(TextureAtlas textureAtlas) {
        int[] textures = new int[1];
        GLES31.glGenTextures(1, textures, 0);
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textures[0]);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, createBitmap(textureAtlas), 0);

        List<Region> regions = textureAtlas.getRegions();
        textureCoordsCacheArray = new float[regions.size() * 8];
        final int k = 8;
        float atlasWidth = textureAtlas.getWidth();
        float atlasHeight = textureAtlas.getHeight();
        for (int i = 0; i < regions.size(); i++) {
            Region region = regions.get(i);
            GLUtils.texSubImage2D(GLES31.GL_TEXTURE_2D, 0, region.x, region.y, region.bitmap);
            float x0 = region.x / atlasWidth;
            float y0 = region.y / atlasHeight;
            float x1 = x0 + region.bitmap.getWidth() / atlasWidth;
            float y1 = y0 + region.bitmap.getHeight() / atlasHeight;
            List<Float> coords = Arrays.asList(x0, y0, x0, y1, x1, y1, x1, y0);
            if (region.cwRotated) {
                Collections.rotate(coords, 2);
            }
            for (int j = 0; j < coords.size(); j++) {
                textureCoordsCacheArray[i * k + j] = coords.get(j);
            }
        }
    }

    private Bitmap createBitmap(TextureAtlas textureAtlas) {
        return Bitmap.createBitmap(textureAtlas.getWidth(), textureAtlas.getHeight(), Bitmap.Config.ARGB_8888);
    }

    private void setupBuffers() {
        int maxCount = particleSystem.getMaxCount();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxCount * 8 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexArray = new float[maxCount * 8];

        byteBuffer = ByteBuffer.allocateDirect(maxCount * 8 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureCoordsBuffer = byteBuffer.asFloatBuffer();
        textureCoordsArray = new float[maxCount * 8];

        byteBuffer = ByteBuffer.allocateDirect(maxCount * 4 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        alphaBuffer = byteBuffer.asFloatBuffer();
        alphaArray = new float[maxCount * 4];

        byteBuffer = ByteBuffer.allocateDirect(maxCount * 6 * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        drawListBuffer = byteBuffer.asShortBuffer();
        fillDrawListBuffer(maxCount);
        drawListBuffer.position(0);
    }

    private void fillDrawListBuffer(int count) {
        int k = 4;
        for (int i = 0; i < count; i++) {
            drawListBuffer.put((short) (i * k));
            drawListBuffer.put((short) (i * k + 1));
            drawListBuffer.put((short) (i * k + 2));
            drawListBuffer.put((short) (i * k));
            drawListBuffer.put((short) (i * k + 2));
            drawListBuffer.put((short) (i * k + 3));
        }
    }

    private void updateBuffers(List<? extends Particle> particles) {
        final int k1 = 8;
        final int k2 = 4;
        for (int i = 0, count = particles.size(); i < count; i++) {
            Particle particle = particles.get(i);
            vertexArray[i * k1] = particle.getX() - particle.getDx2();
            vertexArray[i * k1 + 1] = surfaceHeight - particle.getY() + particle.getDy2();
            vertexArray[i * k1 + 2] = particle.getX() - particle.getDx1();
            vertexArray[i * k1 + 3] = surfaceHeight - particle.getY() - particle.getDy1();
            vertexArray[i * k1 + 4] = particle.getX() + particle.getDx2();
            vertexArray[i * k1 + 5] = surfaceHeight - particle.getY() - particle.getDy2();
            vertexArray[i * k1 + 6] = particle.getX() + particle.getDx1();
            vertexArray[i * k1 + 7] = surfaceHeight - particle.getY() + particle.getDy1();

            System.arraycopy(textureCoordsCacheArray, particle.getTextureIndex() * k1, textureCoordsArray, i * k1, k1);

            alphaArray[i * k2] = particle.getAlpha();
            alphaArray[i * k2 + 1] = particle.getAlpha();
            alphaArray[i * k2 + 2] = particle.getAlpha();
            alphaArray[i * k2 + 3] = particle.getAlpha();
        }
        vertexBuffer.put(vertexArray);
        textureCoordsBuffer.put(textureCoordsArray);
        alphaBuffer.put(alphaArray);
        vertexBuffer.position(0);
        textureCoordsBuffer.position(0);
        alphaBuffer.position(0);
    }

    private void render(int count) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT | GLES31.GL_DEPTH_BUFFER_BIT);

        int matrixHandle = GLES31.glGetUniformLocation(program, "uMvpMatrix");
        GLES31.glUniformMatrix4fv(matrixHandle, 1, false, projectionViewM, 0);

        int positionHandle = GLES31.glGetAttribLocation(program, "aPosition");
        GLES31.glEnableVertexAttribArray(positionHandle);
        GLES31.glVertexAttribPointer(positionHandle, 2, GLES31.GL_FLOAT, false, 0, vertexBuffer);

        int textureHandle = GLES31.glGetUniformLocation(program, "uTexture");
        GLES31.glUniform1i(textureHandle, 0);

        int textureCoordsHandle = GLES31.glGetAttribLocation(program, "aTextureCoords");
        GLES31.glEnableVertexAttribArray(textureCoordsHandle);
        GLES31.glVertexAttribPointer(textureCoordsHandle, 2, GLES31.GL_FLOAT, false, 0, textureCoordsBuffer);

        int alphaHandle = GLES31.glGetAttribLocation(program, "aAlpha");
        GLES31.glEnableVertexAttribArray(alphaHandle);
        GLES31.glVertexAttribPointer(alphaHandle, 1, GLES31.GL_FLOAT, false, 0, alphaBuffer);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, count * 6, GLES31.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES31.glDisableVertexAttribArray(positionHandle);
        GLES31.glDisableVertexAttribArray(alphaHandle);
        GLES31.glDisableVertexAttribArray(textureCoordsHandle);
    }

}
