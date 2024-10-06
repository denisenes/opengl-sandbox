package engine.shaders

import engine.GLExt.{ProgramH, ShaderH, ULocation}
import engine.LOG
import engine.shaders.ShaderProgram.loadShader
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.{GL11, GL20}
import org.lwjgl.opengl.GL20.*

import scala.io.Source

object ShaderProgram:

    private def loadShader(file: String, tpe: Int): ShaderH =
        val raw    = Source.fromFile(file)
        val source = raw.getLines().mkString("\n")

        val shaderID = ShaderH.create(tpe)
        glShaderSource(!shaderID, source)

        shaderID.compile()
        if glGetShaderi(!shaderID, GL_COMPILE_STATUS) == GL_FALSE then
            println(glGetShaderInfoLog(!shaderID, 500))
            LOG("Could not compile shader.")
            System.exit(1)

        LOG(s"Successfully loaded shader from file ${file}")
        shaderID

abstract class ShaderProgram(val vertexFile: String, val fragmentFile: String) extends AutoCloseable:

    private var programID:       ProgramH = ProgramH.NOT_INITIALIZED
    private var vertexShaderID:   ShaderH = ShaderH.NOT_INITIALIZED
    private var fragmentShaderID: ShaderH = ShaderH.NOT_INITIALIZED

    vertexShaderID   = loadShader(vertexFile, GL_VERTEX_SHADER)
    fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER)
    programID        = ProgramH.create()

    glAttachShader(!programID, !vertexShaderID)
    glAttachShader(!programID, !fragmentShaderID)

    bindAttributes()

    glLinkProgram(!programID)
    glValidateProgram(!programID)

    protected def bindAttributes(): Unit

    protected def bindAttribute(attribute: Int, variableName: String): Unit =
        glBindAttribLocation(!programID, attribute, variableName)

    protected def getUniformLocation(uniformName: String): ULocation =
        LOG(s"[Shader program] get uniform location, id=${!programID}, name=$uniformName")
        ULocation.getByName(programID, uniformName)

    def start(): Unit = glUseProgram(!programID)

    def stop(): Unit = glUseProgram(0)

    def close(): Unit =
        stop()
        vertexShaderID.destroy(programID)
        fragmentShaderID.destroy(programID)
        programID.destroy()
