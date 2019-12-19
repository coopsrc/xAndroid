package com.coopsrc.android.particle;

import android.graphics.Bitmap;
import android.graphics.Shader;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.coopsrc.android.particle.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class ParticleRenderer implements GLSurfaceView.Renderer {

    private static final double NANOSECONDS = 1000000000;

    private volatile ParticleSystem particleSystem;
    private volatile boolean particleSystemNeedsSetup;
    private volatile TextureAtlasFactory textureAtlasFactory;
    private volatile boolean textureAtlasNeedsSetup;
    private int surfaceHeight;
    private int programRef;
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
        programRef = ShaderUtils.linkProgram(vertexShader, fragmentShader);
        GLES31.glUseProgram(programRef);
    }

    public void setTextureAtlasFactory(TextureAtlasFactory factory) {
        this.textureAtlasFactory = factory;
        textureAtlasNeedsSetup = true;
    }

    public void setParticleSystem(ParticleSystem system) {
        this.particleSystem = system;
        particleSystemNeedsSetup = true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
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
            TextureAtlas atlas = textureAtlasFactory.createTextureAtlas();
            atlas.setEditable(false);
            setupTextures(atlas);
            textureAtlasNeedsSetup = false;
        }
        long time = System.nanoTime();
        if (lastUpdateTime == 0) {
            lastUpdateTime = time;
        }
        List<? extends Particle> particles = particleSystem.update((time - lastUpdateTime) / NANOSECONDS);
        lastUpdateTime = time;
        updateBuffers(particles);
        render(particles.size());
    }

    private void setupTextures(TextureAtlas atlas) {
        int[] names = new int[1];
        GLES31.glGenTextures(1, names, 0);
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, names[0]);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, Bitmap.createBitmap(atlas.getWidth(), atlas.getHeight(),
                Bitmap.Config.ARGB_8888), 0);

        List<TextureAtlas.Region> regions = atlas.getRegions();
        textureCoordsCacheArray = new float[regions.size() * 8];
        final int k = 8;
        float atlasWidth = atlas.getWidth();
        float atlasHeight = atlas.getHeight();
        for (int i = 0; i < regions.size(); i++) {
            TextureAtlas.Region r = regions.get(i);
            GLUtils.texSubImage2D(GLES31.GL_TEXTURE_2D, 0, r.x, r.y, r.bitmap);
            float x0 = r.x / atlasWidth;
            float y0 = r.y / atlasHeight;
            float x1 = x0 + r.bitmap.getWidth() / atlasWidth;
            float y1 = y0 + r.bitmap.getHeight() / atlasHeight;
            List<Float> coords = Arrays.asList(x0, y0, x0, y1, x1, y1, x1, y0);
            if (r.cwRotated) {
                Collections.rotate(coords, 2);
            }
            for (int j = 0; j < coords.size(); j++) {
                textureCoordsCacheArray[i * k + j] = coords.get(j);
            }
        }
    }

    private void setupBuffers() {
        int maxCount = particleSystem.getMaxCount();

        ByteBuffer b = ByteBuffer.allocateDirect(maxCount * 8 * 4);
        b.order(ByteOrder.nativeOrder());
        vertexBuffer = b.asFloatBuffer();
        vertexArray = new float[maxCount * 8];

        b = ByteBuffer.allocateDirect(maxCount * 8 * 4);
        b.order(ByteOrder.nativeOrder());
        textureCoordsBuffer = b.asFloatBuffer();
        textureCoordsArray = new float[maxCount * 8];

        b = ByteBuffer.allocateDirect(maxCount * 4 * 4);
        b.order(ByteOrder.nativeOrder());
        alphaBuffer = b.asFloatBuffer();
        alphaArray = new float[maxCount * 4];

        b = ByteBuffer.allocateDirect(maxCount * 6 * 2);
        b.order(ByteOrder.nativeOrder());
        drawListBuffer = b.asShortBuffer();
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
            Particle p = particles.get(i);
            vertexArray[i * k1] = p.getX() - p.getDx2();
            vertexArray[i * k1 + 1] = surfaceHeight - p.getY() + p.getDy2();
            vertexArray[i * k1 + 2] = p.getX() - p.getDx1();
            vertexArray[i * k1 + 3] = surfaceHeight - p.getY() - p.getDy1();
            vertexArray[i * k1 + 4] = p.getX() + p.getDx2();
            vertexArray[i * k1 + 5] = surfaceHeight - p.getY() - p.getDy2();
            vertexArray[i * k1 + 6] = p.getX() + p.getDx1();
            vertexArray[i * k1 + 7] = surfaceHeight - p.getY() + p.getDy1();

            System.arraycopy(textureCoordsCacheArray, p.getTextureIndex() * k1, textureCoordsArray, i * k1, k1);

            alphaArray[i * k2] = p.getAlpha();
            alphaArray[i * k2 + 1] = p.getAlpha();
            alphaArray[i * k2 + 2] = p.getAlpha();
            alphaArray[i * k2 + 3] = p.getAlpha();
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

        int matrixHandle = GLES31.glGetUniformLocation(programRef, "uMvpMatrix");
        GLES31.glUniformMatrix4fv(matrixHandle, 1, false, projectionViewM, 0);

        int positionHandle = GLES31.glGetAttribLocation(programRef, "aPosition");
        GLES31.glEnableVertexAttribArray(positionHandle);
        GLES31.glVertexAttribPointer(positionHandle, 2, GLES31.GL_FLOAT, false, 0, vertexBuffer);

        int textureHandle = GLES31.glGetUniformLocation(programRef, "uTexture");
        GLES31.glUniform1i(textureHandle, 0);

        int textureCoordsHandle = GLES31.glGetAttribLocation(programRef, "aTextureCoords");
        GLES31.glEnableVertexAttribArray(textureCoordsHandle);
        GLES31.glVertexAttribPointer(textureCoordsHandle, 2, GLES31.GL_FLOAT, false, 0, textureCoordsBuffer);

        int alphaHandle = GLES31.glGetAttribLocation(programRef, "aAlpha");
        GLES31.glEnableVertexAttribArray(alphaHandle);
        GLES31.glVertexAttribPointer(alphaHandle, 1, GLES31.GL_FLOAT, false, 0, alphaBuffer);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, count * 6, GLES31.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES31.glDisableVertexAttribArray(positionHandle);
        GLES31.glDisableVertexAttribArray(alphaHandle);
        GLES31.glDisableVertexAttribArray(textureCoordsHandle);
    }

}
