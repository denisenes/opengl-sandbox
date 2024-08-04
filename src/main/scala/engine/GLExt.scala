package engine

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.{glfwCreateWindow, glfwDestroyWindow}
import org.lwjgl.opengl.GL11.{GL_TEXTURE_2D, glBindTexture, glDeleteTextures, glGenTextures}
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.{GL15, GL20, GL30}

import scala.annotation.targetName

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
        def create(): VaoH = VaoH(GL30.glGenVertexArrays())

    class VaoH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def destroy(): Unit = GL15.glDeleteBuffers(handle)

    object VboH:
        def create(): VboH = VboH(GL15.glGenBuffers())

    class VboH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def destroy(): Unit = GL15.glDeleteBuffers(handle)

    object ShaderH:
        val NOT_INITIALIZED: ShaderH = ShaderH(-1)
        def create(tpe: Int): ShaderH = ShaderH(GL20.glCreateShader(tpe))

    class ShaderH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def compile(): Unit = GL20.glCompileShader(handle)
        def destroy(program: ProgramH):Unit =
            GL20.glDetachShader(!program, handle)
            GL20.glDeleteShader(handle)

    object ProgramH:
        val NOT_INITIALIZED: ProgramH = ProgramH(-1)
        def create(): ProgramH = ProgramH(GL20.glCreateProgram())

    class ProgramH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def destroy(): Unit = GL20.glDeleteProgram(handle)

    object TextureH:
        def create(): TextureH = TextureH(glGenTextures())

    class TextureH(private val handle: Int) extends AnyVal:
        @targetName("!") def unary_! : Int = handle
        def destroy(): Unit = glDeleteTextures(handle)

    def GL_bindBuffer(vboID: VboH, target: Int, unbind: Boolean)(f: => Unit): Unit =
        glBindBuffer(target, !vboID)
        f
        if unbind then glBindBuffer(target, 0)

    def GL_bindVertexArray(vaoID: VaoH)(f: => Unit): Unit =
        glBindVertexArray(!vaoID)
        f
        glBindVertexArray(0)

    def GL_bindTexture(target: Int, texture: TextureH)(f: => Unit): Unit =
        glBindTexture(GL_TEXTURE_2D, !texture)
        f
        glBindTexture(GL_TEXTURE_2D, 0)

    def GL_enableVertexAttribArray(attrNum: Int)(f: => Unit): Unit =
        glEnableVertexAttribArray(0)
        f
        glDisableVertexAttribArray(0)