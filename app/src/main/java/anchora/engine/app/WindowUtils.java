package anchora.engine.app;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.Callbacks.*;

import static anchora.engine.app.ShaderUtils.*;

public class WindowUtils {

    // Window Properties
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String TITLE = "LWJGL Window";

    // GLFW window handle
    private long window;

    // Shader Variables
    private int vertexShaderId, fragmentShaderId, shaderProgramId;

    // VAO and VBO Variables
    private int VAOId;
    private int VBOId;

    

    public void run() {
        // ======================================================
        // LWJGL Initialization
        // ======================================================

        init();
        loop();

        // ======================================================
        // LWJGL Cleanup and Exit
        // ======================================================

        // Release resources when the window is closed
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and release the error callback
        glfwTerminate();
        GLFWErrorCallback prevCallback = glfwSetErrorCallback(null);
        if (prevCallback != null) {
            prevCallback.free();
        }
    }

    private void init() {
        // ======================================================
        // GLFW Window Setup
        // ======================================================

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        System.out.println("Creating window...");
        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center the window on the screen
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,
                (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Create capabilities for the current context
        GL.createCapabilities();

        // // Enable v-sync (removed for aesthedics ;3)
        // glfwSwapInterval(1);

        // ======================================================
        // OpenGL Shader and Vertices Setup
        // ======================================================

        // Define vertices data
        float[] vertexArray = {
            // Positions            // Colors
            -0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, // Bottom left vertex  1
             0.5f, -0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, // Bottom right vertex 2
             0.0f,  0.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f  // Top vertex          3
        };

        // shader source code
        String vertexShaderSource;
        String fragmentShaderSource;

        try {
            vertexShaderSource = 
                new String(Files.readAllBytes(Paths.get("app/shaders/vertex.glsl")));
            fragmentShaderSource = 
                new String(Files.readAllBytes(Paths.get("app/shaders/fragment.glsl")));
        } catch (IOException e) {
            System.err.println("IOException message: " + e.getMessage());
            throw new RuntimeException("Error reading shader files", e);
        }

        // Vertex Shader Setup
        vertexShaderId = loadShader(GL20.GL_VERTEX_SHADER,
        vertexShaderSource, "vertex");

        // Fragment Shader Setup
        fragmentShaderId = loadShader(GL20.GL_FRAGMENT_SHADER,
        fragmentShaderSource, "fragment");

        // Shader Program Setup
        shaderProgramId = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgramId, vertexShaderId);
        GL20.glAttachShader(shaderProgramId, fragmentShaderId);
        GL20.glLinkProgram(shaderProgramId);

        // Check for shader program linking errors
        int linkStatus = GL20.glGetProgrami(shaderProgramId, GL20.GL_LINK_STATUS);
        if (linkStatus != GL11.GL_TRUE) {
            String errorLog = GL20.glGetProgramInfoLog(shaderProgramId);
            throw new RuntimeException("Shader program linking failed:\n" + errorLog);
        }

        // ======================================================
        // OpenGL VAO and VBO Setup
        // ======================================================

        // Generate FloatBuffer
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        verticesBuffer.put(vertexArray);
        verticesBuffer.flip();

        // Generate VBO
        int vertexVBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        // Generate VAO
        VAOId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAOId);

        // ======================================================
        // Specify the layout of the vertex data & Position Attributes
        // ======================================================
        
        // Attribute sizes
        int positionSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionSize + colorSize) * Float.BYTES;

        // Specify the layout of the position data
        GL20.glVertexAttribPointer(0, 
            positionSize, GL11.GL_FLOAT, false, 
            vertexSizeBytes, 0);
        GL20.glEnableVertexAttribArray(0);

        // Specify the layout of the color data
        GL20.glVertexAttribPointer(1, 
            colorSize, GL11.GL_FLOAT, false, 
            vertexSizeBytes, positionSize);
        GL20.glEnableVertexAttribArray(1);

        // Use your shader program to draw the rectangle
        GL20.glUseProgram(shaderProgramId);
        GL30.glBindVertexArray(VAOId);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // Set the clear color
        System.out.println("Setting clear color..." + window);
        glClearColor(0.0f, 1.0f, 1.0f, 0.0f);

        // Run the rendering loop until the user closes the window
        while (!glfwWindowShouldClose(window)) {
            // Clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT);

            // Use your shader program to draw the rectangle
            GL20.glUseProgram(shaderProgramId);
            GL30.glBindVertexArray(VAOId);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

            // Poll for events and swap the buffers
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

}
