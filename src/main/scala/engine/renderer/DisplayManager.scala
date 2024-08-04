package engine.renderer

import engine.GLExt.WindowH
import org.lwjgl.*
import org.lwjgl.glfw.*
import org.lwjgl.opengl.*
import org.lwjgl.system.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryStack.*
import org.lwjgl.system.MemoryUtil.*

import java.nio.IntBuffer
import scala.util.Using

object DisplayManager:

    private val WIDTH  = 1280
    private val HEIGHT = 720
    private val TITLE  = "Scala Shitty Engine"

    def createDisplay(): DisplayManager =
        println("LWJGL " + Version.getVersion)

        GLFWErrorCallback.createPrint(System.err).set()

        if !glfwInit() then
            throw new IllegalStateException()

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

        val window = WindowH.create(WIDTH, HEIGHT, TITLE, NULL, NULL)
        if !window == NULL then
            throw new RuntimeException("Failed to create GLFW window")

        glfwSetKeyCallback(!window,
            (window, key, scancode, action, mods) =>
                if (key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(window, true)
        )

        Using(stackPush()) { stack =>
            val pWidth: IntBuffer = stack.mallocInt(1)
            val pHeight: IntBuffer = stack.mallocInt(1)

            glfwGetWindowSize(!window, pWidth, pHeight)

            val vidmode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
            glfwSetWindowPos(!window,
                (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2)
        }

        glfwMakeContextCurrent(!window)
        glfwSwapInterval(1)
        glfwShowWindow(!window)

        GL.createCapabilities()
        new DisplayManager(window)

class DisplayManager(val window: WindowH) extends AutoCloseable:

    def updateDisplay(): Unit =
        glfwSwapBuffers(!window)
        glfwPollEvents()

    def getWindowWidth(): Int =
        val w = BufferUtils.createIntBuffer(1)
        glfwGetWindowSize(!window, w, null)
        w.get(0)

    def getWindowHeight(): Int =
        val h = BufferUtils.createIntBuffer(1)
        glfwGetWindowSize(!window, h, null)
        h.get(0)

    private def closeDisplay(): Unit =
        window.destroy()
        glfwTerminate()
        glfwSetErrorCallback(null).free()

    override def close(): Unit = closeDisplay()
