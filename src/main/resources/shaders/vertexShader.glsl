#version 400 core

in  vec3 position;
in  vec2 textCoords;

out vec2 out_textCoords;

uniform mat4 transMtx;
uniform mat4 projMtx;

void main(void) {
    gl_Position = projMtx * transMtx * vec4(position, 1.0);
    out_textCoords = textCoords;
}
