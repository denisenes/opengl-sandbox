#version 400 core

in  vec3 color;
out vec4 pixel_color;

void main(void) {
    pixel_color = vec4(color, 1.0);
}