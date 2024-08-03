package engine.shaders

import engine.Utils.{ProgramID, ShaderID}
import engine.shaders.ShaderProgram.loadShader
import org.lwjgl.opengl.{GL11, GL20}

import scala.io.Source

object ShaderProgram:

    private def loadShader(file: String, tpe: Int): ShaderID =
        val raw    = Source.fromFile(file)
        val source = raw.getLines().mkString("\n")

        val shaderID = GL20.glCreateShader(tpe)
        GL20.glShaderSource(shaderID, source)

        GL20.glCompileShader(shaderID)
        if GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE then
            println(GL20.glGetShaderInfoLog(shaderID, 500))
            System.err.print("Could not compile shader.")
            System.exit(1)

        println(s"Successfully loaded shader from file ${file}")
        shaderID

abstract class ShaderProgram(val vertexFile: String, val fragmentFile: String) extends AutoCloseable:

    private var programID: ProgramID = -1
    private var vertexShaderID: ShaderID = -1
    private var fragmentShaderID: ShaderID = -1

    vertexShaderID   = loadShader(vertexFile, GL20.GL_VERTEX_SHADER)
    fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER)
    programID        = GL20.glCreateProgram()

    GL20.glAttachShader(programID, vertexShaderID)
    GL20.glAttachShader(programID, fragmentShaderID)
    GL20.glLinkProgram(programID)
    GL20.glValidateProgram(programID)
    bindAttributes()

    protected def bindAttributes(): Unit

    protected def bindAttribute(attribute: Int, variableName: String): Unit =
        GL20.glBindAttribLocation(programID, attribute, variableName)

    def start(): Unit = GL20.glUseProgram(programID)

    def stop(): Unit = GL20.glUseProgram(0)

    def close(): Unit =
        stop()
        GL20.glDetachShader(programID, vertexShaderID)
        GL20.glDetachShader(programID, fragmentShaderID)
        GL20.glDeleteShader(vertexShaderID)
        GL20.glDeleteShader(fragmentShaderID)
        GL20.glDeleteProgram(programID)
