#version 310 es

varying lowp float vAlpha;
uniform mediump sampler2D uTexture;
varying mediump vec2 vTextureCoords;

void main() {
    gl_FragColor = texture2D(uTexture, vTextureCoords) * vAlpha;
}








