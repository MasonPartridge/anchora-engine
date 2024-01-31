package anchora.engine.app;

public class VerticesUtils {

    public static float[] generateHexagon(
        float sideLength, float centerX, float centerY, int singleVertexArrayLength) {

        float[] verticies = new float[6 * singleVertexArrayLength];

        // Calculate vertices for the hexagon
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * i;
            verticies[i * singleVertexArrayLength] = 
                (int) (centerX + sideLength * Math.cos(angle));
            verticies[i * singleVertexArrayLength + 1] = 
                (int) (centerY + sideLength * Math.sin(angle));
        }

        return verticies;
    }
}
