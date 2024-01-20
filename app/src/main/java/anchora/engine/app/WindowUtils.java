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

    // Shader program
    private int shaderProgram;
    private int VAO;
    private int VBO;

    public void run() {
        init();
        loop();

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
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // The window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // The window will be resizable

        System.out.println("Creating window...");
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center the window on the screen
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Create capabilities for the current context
        GL.createCapabilities();

        // // Enable v-sync (removed for aesthedics ;3)
        // glfwSwapInterval(1);

        // Load the shaders
        int vertexShader = loadShader(GL20.GL_VERTEX_SHADER, "app/shaders/vertex.glsl");
        int fragmentShader = loadShader(GL20.GL_FRAGMENT_SHADER, "app/shaders/fragment.glsl");

        // Create a shader program and link your shaders to it
        shaderProgram = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgram, vertexShader);
        GL20.glAttachShader(shaderProgram, fragmentShader);
        GL20.glLinkProgram(shaderProgram);

        float[] vertices = {
                -0.5f, -0.5f, 0.0f, // Bottom left vertex
                0.5f, -0.5f, 0.0f, // Bottom right vertex
                0.0f, 0.5f, 0.0f // Top vertex
        };

        float[] colors = {
                1.0f, 0.0f, 0.0f, 1.0f, // Red color for the first vertex
                0.0f, 1.0f, 0.0f, 1.0f, // Green color for the second vertex
                0.0f, 0.0f, 1.0f, 1.0f // Blue color for the third vertex
        };

        int colorVBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorVBO);

        GL20.glEnableVertexAttribArray(1); // Enable the second attribute. The first attribute (0) is the vertex
                                           // positions.
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0); // Tell OpenGL that the color data is structured
                                                                      // as 4 floats per vertex.
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorBuffer.put(colors);
        colorBuffer.flip();

        // Define vertex data
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        // Create a VAO and VBO, and define your vertex data
        VAO = GL30.glGenVertexArrays();
        VBO = GL15.glGenBuffers();
        GL30.glBindVertexArray(VAO);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

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
