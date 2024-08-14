package engine.shaders

import engine.Utils
import engine.Utils.Paths
import engine.shaders.StaticShader.{FRAGMENT_FILE, VERTEX_FILE}

import scala.annotation.static

object StaticShader:
    @static val VERTEX_FILE   = Utils.Paths.SHADERS_DIR + "vertexShader.glsl"
    @static val FRAGMENT_FILE = Utils.Paths.SHADERS_DIR + "fragmentShader.glsl"

class StaticShader extends ShaderProgram(VERTEX_FILE, FRAGMENT_FILE):

    override protected def bindAttributes(): Unit =
        super.bindAttribute(0, "position")
        super.bindAttribute(1, "textCoords")