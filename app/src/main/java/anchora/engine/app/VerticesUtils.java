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
}
