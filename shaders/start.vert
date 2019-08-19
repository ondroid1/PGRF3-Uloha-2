#version 150
in vec2 inPosition; // vstupní pozice
in vec2 inTextureCoordinates; // vstupní souřadnice textury
out vec3 vertexColor; // výstupní barva
out vec2 textureCoordinates; // výstupní souřadnice textury

uniform sampler2D waveHeightTextureID;
uniform sampler2D mainTextureID;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float time;
uniform int showWaves;

void main() {
	vec2 pos = inPosition * 8;
	vec3 finalPos = vec3(pos.x, pos.y, 0);

	if (showWaves > 0) {
		vec2 offset1 = vec2(1.0, 0.5) * time * 0.1;
		vec2 offset2 = vec2(0.5, 1.0) * time * 0.1;
		float height1 = texture2D(waveHeightTextureID, inTextureCoordinates + offset1).r * 0.2;
		float height2 = texture2D(waveHeightTextureID, inTextureCoordinates + offset2).r * 0.2;
		finalPos.z = height1 + height2;
	} else {
		//finalPos.z = finalPos.z - 1;
	}

	textureCoordinates = inTextureCoordinates;

	gl_Position = projectionMatrix * viewMatrix * vec4(finalPos, 1.0);
}

