#version 150
in vec3 vertexColor; // vstup z proběhlé části pipeline (vertex shaderu)
in vec2 textureCoordinates;
out vec4 outColor; // výstup z  fragment shaderu

uniform sampler2D mainTextureID;
uniform int showTexture;

void main() {
    if (showTexture == 1) {
        outColor = texture(mainTextureID, textureCoordinates);
    } else {
        outColor = vec4(0, 0, 1, 1.0); // blue
    }
    //outColor = vec4(vec3(1.0), 1.0);
} 
