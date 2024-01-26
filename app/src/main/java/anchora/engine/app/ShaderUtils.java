package anchora.engine.app;

import org.lwjgl.opengl.GL20;

public class ShaderUtils {

    
    public static int loadShader(int type,
            String shaderSource, String shaderName) {

        // Check if inputs are valid
        if (shaderSource == null || shaderName == null || type == 0) {
            throw new IllegalArgumentException("ShaderUtils: Invalid shader inputs.");
        }

        // Create shader object
        int shader = GL20.glCreateShader(type);
        if (shader == 0) {
            throw new RuntimeException("ShaderUtils: Failed to create shader: "
                    + shaderName);
        }
        GL20.glShaderSource(shader, shaderSource);
        GL20.glCompileShader(shader);

        // Check for errors
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            throw new RuntimeException("ShaderUtils: Could not compile "
                    + shaderName + " shader. Reason:\n"
                    + GL20.glGetShaderInfoLog(shader));

        }

        return shader;
    }
}
