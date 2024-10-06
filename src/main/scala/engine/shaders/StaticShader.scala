package engine.shaders

import engine.GLExt.ULocation
import engine.{LOG, Utils}
import engine.Utils.Paths
import engine.shaders.StaticShader.{FRAGMENT_FILE, VERTEX_FILE}
import org.joml.Matrix4f

import scala.annotation.static

object StaticShader:
    @static private val VERTEX_FILE   = Utils.Paths.SHADERS_DIR + "vertexShader.glsl"
    @static private val FRAGMENT_FILE = Utils.Paths.SHADERS_DIR + "fragmentShader.glsl"

class StaticShader extends ShaderProgram(VERTEX_FILE, FRAGMENT_FILE):

    private var transMtxLoc: ULocation = super.getUniformLocation("transMtx")
    private var projMtxLoc: ULocation  = super.getUniformLocation("projMtx")

    override protected def bindAttributes(): Unit =
        LOG("[Static shader] bind attributes")
        super.bindAttribute(0, "position")
        super.bindAttribute(1, "textCoords")

    def loadTransformationMatrix(mtx: Matrix4f): Unit =
        LOG("[Static shader] load transformation matrix")
        transMtxLoc.loadMatrix(mtx)

    def loadProjectionMatrix(mtx: Matrix4f): Unit =
        LOG("[Static shader] load projection matrix")
        projMtxLoc.loadMatrix(mtx)