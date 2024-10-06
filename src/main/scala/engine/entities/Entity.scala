package engine.entities

import engine.models.TexturedModel
import org.joml.Vector3f

class Entity(
    val model: TexturedModel,
    val position: Vector3f,
    var rotX: Float, var rotY: Float, var rotZ: Float,
    var scale: Float):

    def changePosition(dx: Float, dy: Float, dz: Float): Unit = position.add(dx, dy, dz)

    def changeRotation(dx: Float, dy: Float, dz: Float): Unit =
        rotX = rotX + dx
        rotY = rotY + dy
        rotZ = rotZ + dz