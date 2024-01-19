package anchora.engine.app;

import org.lwjgl.opengl.GL20;

public class ShaderUtils {

    public static int loadShader(int type, String shaderSource) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, shaderSource);
        GL20.glCompileShader(shader);
        return shader;
    }
}
