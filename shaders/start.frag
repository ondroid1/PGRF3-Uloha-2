#version 150
in vec3 vertColor; // vstup z proběhlé části pipeline (vertex shaderu)
in vec2 texCoord;
out vec4 outColor; // výstup z  fragment shaderu

uniform sampler2D textureID;

void main() {
	//outColor = vec4(vertColor, 1.0);
	outColor = vec4(0, 0, 1, 1.0); // blue
    outColor = texture(textureID, texCoord);
} 
