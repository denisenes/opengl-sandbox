package engine

import engine.GLExt.ULocation.{UNINITIALIZED, mtxLoadingBuffer}
import engine.shaders.StaticShader
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.{glfwCreateWindow, glfwDestroyWindow}
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.{GL15, GL20, GL30}
import org.joml.{Matrix4f, Vector3f}
import org.lwjgl.BufferUtils

import java.nio.FloatBuffer
import scala.annotation.{showAsInfix, static, targetName}

// TODO add more methods to handles

/**
 * Some (useful?) lwjgl wrappers.
 */
object GLExt:

    object WindowH:
        def create(width: Int, height: Int, title: CharSequence, monitor: Long, share: Long): WindowH =
            WindowH(glfwCreateWindow(width, height, title, monitor, share))

    class WindowH(private val handle: Long) extends AnyVal:
        @targetName("!") def unary_! : Long = handle
        def destroy(): Unit =
            glfwFreeCallbacks(handle)
            glfwDestroyWindow(handle)

    object VaoH:
        def create(): VaoH = VaoH(glGenVertexArrays())

    class VaoH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def destroy(): Unit = glDeleteBuffers(handle)

    object VboH:
        def create(): VboH = VboH(glGenBuffers())

    class VboH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def destroy(): Unit = glDeleteBuffers(handle)

    object ShaderH:
        val NOT_INITIALIZED: ShaderH = ShaderH(-1)
        def create(tpe: Int): ShaderH = ShaderH(glCreateShader(tpe))

    class ShaderH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def compile(): Unit = glCompileShader(handle)
        def destroy(program: ProgramH):Unit =
            glDetachShader(!program, handle)
            glDeleteShader(handle)

    object ProgramH:
        val NOT_INITIALIZED: ProgramH = ProgramH(-1)
        def create(): ProgramH = ProgramH(glCreateProgram())

    class ProgramH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def destroy(): Unit = glDeleteProgram(handle)

    object TextureH:
        def create(): TextureH = TextureH(glGenTextures())

    class TextureH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def destroy(): Unit = glDeleteTextures(handle)

    object ULocation:
        val mtxLoadingBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
        val UNINITIALIZED: ULocation      = ULocation(-1)

        def getByName(programID: ProgramH, name: String): ULocation =
            ULocation(glGetUniformLocation(!programID, name))

    class ULocation(private val location: Int) extends AnyVal:
        private def loc() =
            //assert(this != UNINITIALIZED, "ULocation is uninitialized")
            location

        def loadFloat(value: Float): Unit = glUniform1f(loc(), value)
        def loadVec3(value: Vector3f): Unit = glUniform3f(loc(), value.x, value.y, value.z)
        def loadBool(value: Boolean): Unit = loadFloat(if value then 1f else 0f)
        def loadMatrix(value: Matrix4f): Unit =
            value.get(mtxLoadingBuffer)
            glUniformMatrix4fv(loc(), false, mtxLoadingBuffer)

    def GL_bindBuffer(vboID: VboH, target: Int, unbind: Boolean)(f: => Unit): Unit =
        glBindBuffer(target, !vboID)
        f
        if unbind then glBindBuffer(target, 0)

    def GL_bindVertexArray(vaoID: VaoH)(f: => Unit): Unit =
        glBindVertexArray(!vaoID)
        f
        glBindVertexArray(0)

    def GL_bindTexture(target: Int, texture: TextureH)(f: => Unit): Unit =
        glBindTexture(target, !texture)
        f
        glBindTexture(target, 0)

    def GL_enableVertexAttribArray(attrNum: Int)(f: => Unit): Unit =
        glEnableVertexAttribArray(attrNum)
        f
        glDisableVertexAttribArray(attrNum)

    def GL_withShader(shader: StaticShader)(f: => Unit): Unit =
        shader.start()
        f
        shader.stop()