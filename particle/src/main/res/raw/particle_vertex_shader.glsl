#version 310 es

uniform mat4 uMvpMatrix;
in vec4 aPosition;
in float aAlpha;
in vec2 aTextureCoords;

out float vAlpha;
out vec2 vTextureCoords;

void main() {
    gl_Position = uMvpMatrix * aPosition;
    vAlpha = aAlpha;
    vTextureCoords = aTextureCoords;
}
