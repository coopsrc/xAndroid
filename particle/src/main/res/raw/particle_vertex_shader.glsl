#version 310 es

uniform mat4 uMvpMatrix;
in vec4 aPosition;
in float aAlpha;
out float vAlpha;
in vec2 aTextureCoords;
out vec2 vTextureCoords;

void main() {
    gl_Position = uMvpMatrix * aPosition;
    vAlpha = aAlpha;
    vTextureCoords = aTextureCoords;
}
