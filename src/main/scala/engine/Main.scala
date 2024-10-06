package engine

import engine.entities.Entity
import engine.models.{ModelManager, TexturedModel}
import engine.renderer.{DisplayManager, Renderer}
import engine.shaders.StaticShader
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose

import scala.util.Using

object Main:

    private def mainLoop(): Unit =
        val vertices: Array[Float] = Array(
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f
        )

        val indices: Array[Int] = Array(
            0, 1, 3,
            3, 1, 2
        )

        val textureCoords: Array[Float] = Array(
            0, 0,
            0, 1,
            1, 1,
            1, 0
        )

        Using.Manager { use =>
            LOG("Before initialization")

            val dm     = use(DisplayManager.createDisplay())
            val loader = use(ModelManager)
            val shader = use(StaticShader())

            val renderer = Renderer(dm, shader)

            val model   = loader.loadRawModel(vertices, textureCoords, indices)
            val texture = loader.loadTexture("ground.jpg")
            val texturedModel = TexturedModel(model, texture)

            val entity: Entity = Entity(texturedModel, Vector3f(0, 0, -1), 0, 0, 0, 1)

            LOG("Initialization complete")

            while (!glfwWindowShouldClose(!dm.window)) {
                entity.changePosition(0, 0, -0.01)
                entity.changeRotation(0, 1, 0.5)
                renderer.prepare()
                renderer.render(entity)
                dm.updateDisplay()
            }
        }

    @main def main(): Unit = mainLoop()
