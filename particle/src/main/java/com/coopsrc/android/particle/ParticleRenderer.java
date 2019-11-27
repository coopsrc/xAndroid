package com.coopsrc.android.particle;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

class ParticleRenderer  implements GLSurfaceView.Renderer {

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
        glClearColor(0f, 0f, 0f, 0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void initGlProgram() {
        int vShaderRef = compileShader(GL_VERTEX_SHADER, Shaders.V_SHADER);
        int fShaderRef = compileShader(GL_FRAGMENT_SHADER, Shaders.F_SHADER);
        programRef = glCreateProgram();
        glAttachShader(programRef, vShaderRef);
        glAttachShader(programRef, fShaderRef);
        glLinkProgram(programRef);
        glUseProgram(programRef);
    }

    private int compileShader(int type, String shaderCode) {
        int ref = glCreateShader(type);
        glShaderSource(ref, shaderCode);
        glCompileShader(ref);
        return ref;
    }

    void setTextureAtlasFactory(TextureAtlasFactory factory) {
        this.textureAtlasFactory = factory;
        textureAtlasNeedsSetup = true;
    }

    void setParticleSystem(ParticleSystem system) {
        this.particleSystem = system;
        particleSystemNeedsSetup = true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
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
        glGenTextures(1, names, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, names[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, Bitmap.createBitmap(atlas.getWidth(), atlas.getHeight(),
                Bitmap.Config.ARGB_8888), 0);

        List<TextureAtlas.Region> regions = atlas.getRegions();
        textureCoordsCacheArray = new float[regions.size() * 8];
        final int k = 8;
        float atlasWidth = atlas.getWidth();
        float atlasHeight = atlas.getHeight();
        for (int i = 0; i < regions.size(); i++) {
            TextureAtlas.Region r = regions.get(i);
            GLUtils.texSubImage2D(GL_TEXTURE_2D, 0, r.x, r.y, r.bitmap);
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
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        int matrixHandle = glGetUniformLocation(programRef, "uMvpMatrix");
        glUniformMatrix4fv(matrixHandle, 1, false, projectionViewM, 0);

        int positionHandle = glGetAttribLocation(programRef, "aPosition");
        glEnableVertexAttribArray(positionHandle);
        glVertexAttribPointer(positionHandle, 2, GL_FLOAT, false, 0, vertexBuffer);

        int textureHandle = glGetUniformLocation(programRef, "uTexture");
        glUniform1i(textureHandle, 0);

        int textureCoordsHandle = glGetAttribLocation(programRef, "aTextureCoords");
        glEnableVertexAttribArray(textureCoordsHandle);
        glVertexAttribPointer(textureCoordsHandle, 2, GL_FLOAT, false, 0, textureCoordsBuffer);

        int alphaHandle = glGetAttribLocation(programRef, "aAlpha");
        glEnableVertexAttribArray(alphaHandle);
        glVertexAttribPointer(alphaHandle, 1, GL_FLOAT, false, 0, alphaBuffer);

        glDrawElements(GL_TRIANGLES, count * 6, GL_UNSIGNED_SHORT, drawListBuffer);

        glDisableVertexAttribArray(positionHandle);
        glDisableVertexAttribArray(alphaHandle);
        glDisableVertexAttribArray(textureCoordsHandle);
    }

}
