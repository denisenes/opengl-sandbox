package engine

import engine.models.{ModelManager, RawModel, TexturedModel}
import engine.renderer.{DisplayManager, Renderer, Texture}
import engine.shaders.StaticShader
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose

import scala.util.Using

object Main:

    private def mainLoop(): Unit =
        val renderer: Renderer = new Renderer()

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
            val dm     = use(DisplayManager.createDisplay())
            val loader = use(ModelManager)
            val shader = use(new StaticShader())

            val model   = loader.loadRawModel(vertices, textureCoords, indices)
            val texture = loader.loadTexture("ground.jpg")
            val texturedModel = new TexturedModel(model, texture)

            while (!glfwWindowShouldClose(!dm.window)) {
                // logic
                renderer.prepare()
                shader.start()
                renderer.render(texturedModel)
                shader.stop()
                dm.updateDisplay()
            }
        }

    @main def main(): Unit = mainLoop()
