#version 310 es

uniform mat4 uMvpMatrix;
attribute vec4 aPosition;
attribute lowp float aAlpha;
varying lowp float vAlpha;
attribute mediump vec2 aTextureCoords;
varying mediump vec2 vTextureCoords;

void main() {
    gl_Position = uMvpMatrix * aPosition;
    vAlpha = aAlpha;
    vTextureCoords = aTextureCoords;
}
