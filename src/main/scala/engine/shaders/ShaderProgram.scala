package engine.shaders

import engine.GLExt.{ProgramH, ShaderH}
import engine.shaders.ShaderProgram.loadShader
import org.lwjgl.opengl.{GL11, GL20}

import scala.io.Source

object ShaderProgram:

    private def loadShader(file: String, tpe: Int): ShaderH =
        val raw    = Source.fromFile(file)
        val source = raw.getLines().mkString("\n")

        val shaderID = ShaderH.create(tpe)
        GL20.glShaderSource(!shaderID, source)

        shaderID.compile()
        if GL20.glGetShaderi(!shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE then
            println(GL20.glGetShaderInfoLog(!shaderID, 500))
            System.err.print("Could not compile shader.")
            System.exit(1)

        println(s"Successfully loaded shader from file ${file}")
        shaderID

abstract class ShaderProgram(val vertexFile: String, val fragmentFile: String) extends AutoCloseable:

    private var programID:       ProgramH = ProgramH.NOT_INITIALIZED
    private var vertexShaderID:   ShaderH = ShaderH.NOT_INITIALIZED
    private var fragmentShaderID: ShaderH = ShaderH.NOT_INITIALIZED

    vertexShaderID   = loadShader(vertexFile, GL20.GL_VERTEX_SHADER)
    fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER)
    programID        = ProgramH.create()

    GL20.glAttachShader(!programID, !vertexShaderID)
    GL20.glAttachShader(!programID, !fragmentShaderID)
    GL20.glLinkProgram(!programID)
    GL20.glValidateProgram(!programID)
    bindAttributes()

    protected def bindAttributes(): Unit

    protected def bindAttribute(attribute: Int, variableName: String): Unit =
        GL20.glBindAttribLocation(!programID, attribute, variableName)

    def start(): Unit = GL20.glUseProgram(!programID)

    def stop(): Unit = GL20.glUseProgram(0)

    def close(): Unit =
        stop()
        vertexShaderID.destroy(programID)
        fragmentShaderID.destroy(programID)
        programID.destroy()
