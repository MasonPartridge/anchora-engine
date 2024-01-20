#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;  // Vertex colors

out vec3 vertexColor;

void main() {
    gl_Position = vec4(aPos, 1.0);
    vertexColor = vec3(aPos.x, aPos.y, 1.0);
}
