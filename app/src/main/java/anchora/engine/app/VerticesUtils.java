package anchora.engine.app;

public class VerticesUtils {

    public static float[] generateHexagon(
            float sideLength, float centerX, float centerY, int singleVertexArrayLength) {

        float[] verticies = new float[6 * singleVertexArrayLength];

        // Calculate vertices for the hexagon
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * i;
            verticies[i * singleVertexArrayLength] = (int) (centerX + sideLength * Math.cos(angle));
            verticies[i * singleVertexArrayLength + 1] = (int) (centerY + sideLength * Math.sin(angle));
        }

        return verticies;
    }

    public static float[] generateLine(int x1, int y1, int x2, int y2, float lineWidth, int singleVertexArrayLength) {
        float[] vertices = new float[4 * singleVertexArrayLength];
        vertices[0] = x1;
        vertices[1] = y1 + lineWidth;
        vertices[0 + singleVertexArrayLength] = y1 - lineWidth;
        vertices[1 + singleVertexArrayLength] = x1;
        vertices[0 + singleVertexArrayLength * 2] = y2 + lineWidth;
        vertices[1 + singleVertexArrayLength * 2] = x2;
        vertices[0 + singleVertexArrayLength * 3] = y2 - lineWidth;
        vertices[1 + singleVertexArrayLength * 3] = x2;
        return vertices;

    }

    public static float[] generateRectangle(int x, int y, int width, int height, int singleVertexArrayLength) {
        float[] vertices = new float[4 * singleVertexArrayLength];
        vertices[0] = x;
        vertices[1] = y;
        vertices[0 + singleVertexArrayLength] = x;
        vertices[1 + singleVertexArrayLength] = y + height;
        vertices[0 + singleVertexArrayLength * 2] = x + width;
        vertices[1 + singleVertexArrayLength * 2] = y + height;
        vertices[0 + singleVertexArrayLength * 3] = x + width;
        vertices[1 + singleVertexArrayLength * 3] = y;
        return vertices;
    }

    public static float[] generateTriangle(int x1, int y1, int x2, int y2, int x3, int y3,
            int singleVertexArrayLength) {
        float[] vertices = new float[3 * singleVertexArrayLength];
        vertices[0] = x1;
        vertices[1] = y1;
        vertices[0 + singleVertexArrayLength] = x2;
        vertices[1 + singleVertexArrayLength] = y2;
        vertices[0 + singleVertexArrayLength * 2] = x3;
        vertices[1 + singleVertexArrayLength * 2] = y3;
        return vertices;
    }

    public static float[] generateCircle(int centerX, int centerY, int radius, int singleVertexArrayLength) {
        float[] vertices = new float[360 * singleVertexArrayLength];
        for (int i = 0; i < 360; i++) {
            double angle = 2 * Math.PI / 360 * i;
            vertices[i * singleVertexArrayLength] = (int) (centerX + radius * Math.cos(angle));
            vertices[i * singleVertexArrayLength + 1] = (int) (centerY + radius * Math.sin(angle));
        }
        return vertices;
    }

    public static float[] generatePolygon(int centerX, int centerY, int radius, int sides,
            int singleVertexArrayLength) {
        float[] vertices = new float[sides * singleVertexArrayLength];
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI / sides * i;
            vertices[i * singleVertexArrayLength] = (int) (centerX + radius * Math.cos(angle));
            vertices[i * singleVertexArrayLength + 1] = (int) (centerY + radius * Math.sin(angle));
        }
        return vertices;
    }

    /**
     * Generates a vertex with the specified coordinates and color.
     *
     * @param x                     The x-coordinate of the vertex.
     * @param y                     The y-coordinate of the vertex.
     * @param z                     The z-coordinate of the vertex.
     * @param color                 The color of the vertex, represented as an array of floats with RGBA values.
     * @return                      The generated vertex as an array of floats.
     */
    public static float[] generateVerticies(float x, float y, float z, float[] color, int verticiesAmount) {

        if (color.length != 4) {
            throw new IllegalArgumentException("VerticesUtils: Invalid color input.");
        } else if (verticiesAmount < 1) {
            throw new IllegalArgumentException("VerticesUtils: Invalid verticies amount.");
        } 

        int singleVertexArrayLength = 7;
        float[] vertices = new float[singleVertexArrayLength * verticiesAmount];

        for (int i = 0; i < verticiesAmount; i++) {
            vertices[i * singleVertexArrayLength] = x;
            vertices[i * singleVertexArrayLength + 1] = y;
            vertices[i * singleVertexArrayLength + 2] = z;
            vertices[i * singleVertexArrayLength + 3] = color[0];
            vertices[i * singleVertexArrayLength + 4] = color[1];
            vertices[i * singleVertexArrayLength + 5] = color[2];
            vertices[i * singleVertexArrayLength + 6] = color[3];
        }
        
        return vertices;
    }
}
