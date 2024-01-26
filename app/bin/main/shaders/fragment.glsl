#version 330 core
in vec4 vertexColor;

out vec4 FragColor;

void main() {
    if (length(vertexColor) > 0.0) { // If vertexColor is not black
        FragColor = vertexColor; // Use vertexColor
    } else {
        FragColor = vec4(0.0, 0.0, 0.0, 1.0); // Use white as default
    }
}