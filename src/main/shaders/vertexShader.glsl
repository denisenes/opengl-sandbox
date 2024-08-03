#version 400 core

in  vec3 position;
out vec3 color;

void main(void) {
    gl_Position = vec4(position, 1.0);
    color = vec3(position.x / position.y * 1.0, 0.9, position.y + 0.5);
}
