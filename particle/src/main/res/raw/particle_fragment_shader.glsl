#version 310 es

precision mediump float;

uniform sampler2D uTextureUnit;
in float vAlpha;
in vec2 vTextureCoords;
out vec4 vFragmentColor;

void main() {
    vFragmentColor = texture(uTextureUnit, vTextureCoords) * vAlpha;
}








