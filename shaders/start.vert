#version 150
in vec2 inPosition; // input from the vertex buffer

uniform mat4 proj;
uniform mat4 view;

void main() {
	vec2 pos = inPosition * 8 - 1;
	vec3 finalPos = vec3(pos.x, pos.y, 0);

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
