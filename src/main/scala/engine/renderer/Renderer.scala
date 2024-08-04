package engine.renderer

import engine.GLExt.{GL_bindVertexArray, GL_enableVertexAttribArray}
import engine.models.{RawModel, TexturedModel}
import org.lwjgl.opengl.GL11

class Renderer:

    def prepare(): Unit =
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
        GL11.glClearColor(0, 0, 0, 1.0)

    def render(model: TexturedModel): Unit =
        GL_bindVertexArray(model.raw.vaoId) {
            GL_enableVertexAttribArray(0) {
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.raw.vertexCount, GL11.GL_UNSIGNED_INT, 0)
            }
        }
