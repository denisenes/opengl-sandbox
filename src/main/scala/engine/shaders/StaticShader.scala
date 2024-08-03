package engine.shaders

import engine.shaders.StaticShader.{FRAGMENT_FILE, VERTEX_FILE}

import scala.annotation.static

object StaticShader:
    @static val VERTEX_FILE   = "src/main/shaders/vertexShader.glsl"
    @static val FRAGMENT_FILE = "src/main/shaders/fragmentShader.glsl"

class StaticShader extends ShaderProgram(VERTEX_FILE, FRAGMENT_FILE):

    override protected def bindAttributes(): Unit =
        super.bindAttribute(0, "position")