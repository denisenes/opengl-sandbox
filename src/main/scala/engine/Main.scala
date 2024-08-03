package engine

import engine.renderer.{DisplayManager, RawModel, Renderer}
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

        Using.Manager { use =>
            val dm     = use(DisplayManager.createDisplay())
            val loader = use(RawModel.withLoader())
            val shader = use(new StaticShader())

            val model: RawModel = loader.loadToVAO(vertices, indices)

            while (!glfwWindowShouldClose(dm.window)) {
                // logic
                renderer.prepare()
                shader.start()
                renderer.render(model)
                shader.stop()
                dm.updateDisplay()
            }
        }

    @main def main(): Unit = mainLoop()
