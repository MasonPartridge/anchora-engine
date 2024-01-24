package anchora.engine.app;

import org.lwjgl.opengl.GL20;

public class ShaderUtils {

    public static int loadShader(int type, String shaderSource, String shaderName) {
        // Create shader object
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, shaderSource);
        GL20.glCompileShader(shader);

        // Check for errors
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("ShaderUtils: Could not compile " + shaderName + " shader. Reason:");
            System.err.println(GL20.glGetShaderInfoLog(shader));
            System.exit(-1);
        }

        return shader;
    }
}
