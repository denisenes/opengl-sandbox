package engine

import org.joml.{Matrix4f, Vector3f}
import org.apache.log4j.Logger

object Utils:

    object Paths:
        val SHADERS_DIR  = "src/main/resources/shaders/"
        val TEXTURES_DIR = "src/main/resources/textures/"

    object Math:
        def createTransMtx(translation: Vector3f, rx: Float, ry: Float, rz: Float, scale: Float): Matrix4f =
            Matrix4f()
                .identity()
                .translate(translation)
                .rotate(StrictMath.toRadians(rx).toFloat, new Vector3f(1, 0, 0))
                .rotate(StrictMath.toRadians(ry).toFloat, new Vector3f(0, 1, 0))
                .rotate(StrictMath.toRadians(rz).toFloat, new Vector3f(0, 0, 1))
                .scale(new Vector3f(scale, scale, scale))

class Utils

object LOG:
    private val logger = Logger.getLogger(this.getClass)

    def apply(msg: String): Unit = logger.debug(msg)
    def apply(msg: String, ex: Exception): Unit = logger.debug(msg, ex)