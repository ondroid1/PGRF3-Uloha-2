#version 150
in vec2 inPosition; // vstupní pozice
in vec2 inTextureCoordinates; // vstupní souřadnice textury
out vec3 vertColor; // výstupní barva
out vec2 texCoord; // výstupní souřadnice textury

uniform sampler2D textureID;
uniform sampler2D waveHeightTextureID;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float time;
uniform int showWaves;

void main() {
	vec2 pos = inPosition * 8;
	vec3 finalPos = vec3(pos.x, pos.y, 1.0);

	//vertColor = waveHeightTexture(textureID, texCoord);

	if (showWaves > 0) {
		vec2 offset1 = vec2(1.0, 0.5) * time * 0.1;
		vec2 offset2 = vec2(0.5, 1.0) * time * 0.1;
		float height1 = texture2D(waveHeightTextureID, inTextureCoordinates + offset1).r * 0.2;
		float height2 = texture2D(waveHeightTextureID, inTextureCoordinates + offset2).r * 0.2;
//		float hight1 = offset1.r; // texture2D(inTextureCoordinates, offset1).r * 0.02;
//		float hight2 = offset2.r; // texture2D(inTextureCoordinates, offset2).r * 0.02;
		finalPos.z = height1 + height2;
		//finalPos = vec3(finalPos.x, finalPos.y, waterHeight + waveHeight(finalPos.x, finalPos.y));
	}

	texCoord = inTextureCoordinates;

	gl_Position = projectionMatrix * viewMatrix * vec4(finalPos, 1.0);
}

