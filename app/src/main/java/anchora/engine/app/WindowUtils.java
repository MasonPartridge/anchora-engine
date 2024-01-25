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

import java.nio.FloatBuffer;

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
    private int vertexShader, fragmentShader, shaderProgram;

    private int VAO;
    private int VBO;

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
        float[] vertices = {
            // Positions            // Colors
            -0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, // Bottom left vertex  1
             0.5f, -0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, // Bottom right vertex 2
             0.0f,  0.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f  // Top vertex          3
        };

        // ======================================================
        // OpenGL Vertex Shader Setup
        // ======================================================

        // Vertex Shader Setup
        vertexShader = loadShader(GL20.GL_VERTEX_SHADER,
        "app/shaders/vertex.glsl", "vertex");






        // ======================================================
        // Load the shaders
        fragmentShader = loadShader(GL20.GL_FRAGMENT_SHADER,
                "app/shaders/fragment.glsl", "fragment");

        // Create a shader program and link your shaders to it
        shaderProgram = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgram, vertexShader);
        GL20.glAttachShader(shaderProgram, fragmentShader);
        GL20.glLinkProgram(shaderProgram);


        GL20.glEnableVertexAttribArray(1); // Enable the second attribute. The first attribute (0) is the vertex
                                           // positions.
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0); // Tell OpenGL that the color data is structured
                                                                      // as 4 floats per vertex.

        // Define vertex data
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        // Generate Vertex VAO
        int vertexVBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        // Generate VAO
        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        // Specify the layout of the vertex data
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(0);

        // Use your shader program to draw the rectangle
        GL20.glUseProgram(shaderProgram);
        GL30.glBindVertexArray(VAO);
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
            GL20.glUseProgram(shaderProgram);
            GL30.glBindVertexArray(VAO);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

            // Poll for events and swap the buffers
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

}
