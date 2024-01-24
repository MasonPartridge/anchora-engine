#version 330 core
in vec3 vertexColor;

out vec4 FragColor;

void main() {
    if (length(vertexColor) > 0.0) { // If vertexColor is not black
        FragColor = vec4(vertexColor, 1.0); // Use vertexColor
    } else {
        FragColor = vec4(1.0, 0.0, 0.0, 1.0); // Otherwise, use white as default
    }
}