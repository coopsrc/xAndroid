package com.coopsrc.android.particle.utils;

import android.opengl.GLES31;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-12-19 15:58
 */
public class ShaderUtils {
    private static final String TAG = "ShaderUtils";

    public static int compileVertexShader(String code) {
        return compileShader(GLES31.GL_VERTEX_SHADER, code);
    }

    public static int compileFragmentShader(String code) {
        return compileShader(GLES31.GL_FRAGMENT_SHADER, code);
    }

    private static int compileShader(int type, String code) {
        final int shader = GLES31.glCreateShader(type);
        if (shader != 0) {

            GLES31.glShaderSource(shader, code);
            GLES31.glCompileShader(shader);

            final int[] compileStatus = new int[1];
            GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                String shaderInfoLog = GLES31.glGetShaderInfoLog(shader);
                System.err.println(shaderInfoLog);

                GLES31.glDeleteShader(shader);
                return 0;
            }

            return shader;
        } else {
            return 0;
        }
    }

    public static int linkProgram(int vertexShader, int fragmentShader) {
        final int program = GLES31.glCreateProgram();
        if (program != 0) {
            GLES31.glAttachShader(program, vertexShader);
            GLES31.glAttachShader(program, fragmentShader);
            GLES31.glLinkProgram(program);

            final int[] linkStatus = new int[1];
            GLES31.glGetProgramiv(program, GLES31.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0) {
                String programInfoLog = GLES31.glGetProgramInfoLog(program);
                System.err.println(programInfoLog);
                GLES31.glDeleteProgram(program);
                return 0;
            }

            return program;
        } else {
            return 0;
        }
    }
}
