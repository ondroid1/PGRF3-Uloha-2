package common;

import com.jogamp.opengl.GL2GL3;
import oglutils.OGLBuffers;

/**
 * Třída pro vytváření gridů složených z trojůhelníků
 */
public class GridFactory {


    /**
     * Vytváří grid z trojúhelníků
     * @param gl
     * @param rowVertices počet vrcholů v řádku
     * @param columnVewrtices počet vrcholů ve sloupci
     * @return
     */
    public static OGLBuffers generateGrid (GL2GL3 gl, int rowVertices, int columnVewrtices, boolean hasTexture) {

        float[] vertexBuffer = getVertices(rowVertices, columnVewrtices, hasTexture);
        int[] indexBuffer = getIndices(rowVertices, columnVewrtices);

        OGLBuffers.Attrib[] attributes;

        if (hasTexture) {
            attributes = new OGLBuffers.Attrib[] {
                    new OGLBuffers.Attrib("inPosition", 3),
                    new OGLBuffers.Attrib("inTextureCoordinates", 2)
            };
        } else {
            attributes = new OGLBuffers.Attrib[] {
                    new OGLBuffers.Attrib("inPosition", 3)
            };
        }

        return new OGLBuffers(gl, vertexBuffer, attributes, indexBuffer);
    }

    /**
     * Vytváří Vertex Buffer
     * @param xLength
     * @param yLength
     * @return
     */
    private static float[] getVertices(int xLength, int yLength, boolean hasTexture) {

        int vertexPartCount = hasTexture ? 5 : 3;
        float[] vertexBuffer = new float[xLength * yLength * vertexPartCount];
        int index = 0;
        int z = 0;

        for (int col = 0; col < yLength; col++) {
            float y = col / (float) (yLength - 1);
            for (int row = 0; row < xLength; row++) {
                float x = row / (float)(xLength - 1);
                vertexBuffer[index++] = x;
                vertexBuffer[index++] = y;
                vertexBuffer[index++] = z;

                if (hasTexture) {
                            vertexBuffer[index++] = x;
                            vertexBuffer[index++] = y;
                }
            }
        }

        return vertexBuffer;
    }

    /**
     * Vytváří Index Buffer
     * https://www.learnopengles.com/android-lesson-eight-an-introduction-to-index-buffer-objects-ibos/
     * @param xLength
     * @param yLength
     * @return
     */
    private static int[] getIndices(int xLength, int yLength) {

        final int numStripsRequired = yLength - 1;
        final int numDegensRequired = 2 * (numStripsRequired - 1);
        final int verticesPerStrip = 2 * xLength;

        final int[] indexBuffer = new int[(verticesPerStrip * numStripsRequired)
                + numDegensRequired];

        int index = 0;

        for (int y = 0; y < yLength - 1; y++) {

            if (y > 0) {
                // Degenerate begin: repeat first vertex
                indexBuffer[index++] = y * yLength;
            }

            for (int x = 0; x < xLength; x++) {
                // One part of the strip
                indexBuffer[index++] = (y * yLength) + x;
                indexBuffer[index++] = ((y + 1) * yLength) + x;
            }

            if (y < yLength - 2) {
                // Degenerate end: repeat last vertex
                indexBuffer[index++] = ((y + 1) * yLength) + (xLength - 1);
            }
        }

        return indexBuffer;
    }
}
