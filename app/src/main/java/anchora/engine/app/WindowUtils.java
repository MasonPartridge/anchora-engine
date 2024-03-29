package anchora.engine.app;

import static anchora.engine.app.ShaderUtils.loadShader;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
    private int VAOId, VBOId, EBOId;

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

        float[] vertexArray = {
            // Positions          // Colors
            -1.0f,  -1.0f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, // Bottom left vertex 1
             1.0f,  -1.0f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, // Bottom right vertex 2
             1.0f,   1.0f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f, // Top right vertex 3
            -1.0f,   1.0f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f  // Top left vertex 4
        };

        int[] elementArray = {
                0, 1, 2,
                2, 3, 0
        };

        checkVertexArray(vertexArray);

        // shader source code
        String vertexShaderSource;
        String fragmentShaderSource;

        try {
            vertexShaderSource = new String(Files.readAllBytes(Paths.get(getClass()
                    .getClassLoader().getResource("shaders/vertex.glsl").toURI())));
            fragmentShaderSource = new String(Files.readAllBytes(Paths.get(getClass()
                    .getClassLoader().getResource("shaders/fragment.glsl").toURI())));
        } catch (IOException e) {
            System.err.println("IOException message: " + e.getMessage());
            throw new RuntimeException("Error reading shader files", e);
        } catch (URISyntaxException e) {
            System.err.println("URISyntaxException message: " + e.getMessage());
            throw new RuntimeException("Error parsing shader file URI", e);
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
        // OpenGL VAO, EBO and VBO Setup
        // ======================================================

        // Generate Vertices FloatBuffer
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        verticesBuffer.put(vertexArray);
        verticesBuffer.flip();

        // Generate Vertex Buffer Object (VBO)
        int vertexVBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        // Generate Vertex Array Object VAO
        VAOId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAOId);

        // Element Buffer Object (EBO) Setup
        int EBOId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBOId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementArray, GL15.GL_STATIC_DRAW);

        // ======================================================
        // Specify vertex attribute pointers
        // ======================================================

        // Attribute sizes
        int positionSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionSize + colorSize) * Float.BYTES;

        // Specify the layout of the position data
        GL20.glVertexAttribPointer(0, positionSize,
                GL11.GL_FLOAT, false, vertexSizeBytes, 0);
        checkGLError("glVertexAttribPointer position");

        GL20.glEnableVertexAttribArray(0);
        checkGLError("glEnableVertexAttribArray position");

        // Specify the layout of the color data
        GL20.glVertexAttribPointer(1, colorSize,
                GL11.GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        checkGLError("glVertexAttribPointer color");

        GL20.glEnableVertexAttribArray(1);
        checkGLError("glEnableVertexAttribArray color");

        // Use your shader program to draw the rectangle
        GL20.glUseProgram(shaderProgramId);
        checkGLError("glUseProgram");

        GL30.glBindVertexArray(VAOId);
        checkGLError("glBindVertexArray");

        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
        checkGLError("glDrawElements");

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

            // Enable attribute pointers for index 0 and 1
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);

            GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
            checkGLError("glDrawElements");

            // Disable attribute pointers after drawing
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);

            // Poll for events and swap the buffers
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private boolean isValidColor(float color) {
        return color >= 0.0f && color <= 1.0f;
    }

    private void checkVertexArray(float[] vertexArray) {
        if (vertexArray == null || vertexArray.length == 0 || vertexArray.length % 7 != 0) {
            throw new IllegalArgumentException("Vertex array is null, empty, or has an invalid length.");
        }
        System.out.println("Checking vertex array... Length: " + vertexArray.length);
        for (int i = 3; i < vertexArray.length; i += 7) {
            for (int j = 0; j < 4; j++) {
                float color = vertexArray[i + j];
                if (!isValidColor(color)) {
                    throw new IllegalArgumentException("Invalid color value: " + color);
                }
            }
        }
    }

    private void checkGLError(String glOperation) {
        int error;
        while ((error = GL11.glGetError()) != GL11.GL_NO_ERROR) {
            String errorMessage;
            switch (error) {
                case GL11.GL_INVALID_ENUM:
                    errorMessage = "Invalid enum";
                    break;
                case GL11.GL_INVALID_VALUE:
                    errorMessage = "Invalid value";
                    break;
                case GL11.GL_INVALID_OPERATION:
                    errorMessage = "Invalid operation";
                    break;
                case GL11.GL_OUT_OF_MEMORY:
                    errorMessage = "Out of memory";
                    break;
                default:
                    errorMessage = "Unknown error";
                    break;
            }
            System.err.println(glOperation + ": " + errorMessage);
        }
    }
}
