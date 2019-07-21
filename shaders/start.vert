#version 150
in vec2 inPosition; // vstupní pozice
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;

void main() {
	vec2 pos = inPosition * 8;
	vec3 finalPos = vec3(pos.x, pos.y, 0);

	vertColor = finalPos;
	gl_Position = proj * view * vec4(finalPos, 1.0);
}
