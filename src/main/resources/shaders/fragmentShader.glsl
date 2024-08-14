#version 400 core

in  vec2 out_textCoords;
out vec4 pixel_color;

uniform sampler2D textureSampler;

void main(void) {
    pixel_color = texture(textureSampler, out_textCoords);
}