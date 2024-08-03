package engine.renderer

import engine.Utils.{GL_bindVertexArray, GL_enableVertexAttribArray}
import org.lwjgl.opengl.GL11

class Renderer:

    def prepare(): Unit =
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
        GL11.glClearColor(0, 0, 0, 1.0)

    def render(model: RawModel): Unit =
        GL_bindVertexArray(model.vaoId) {
            GL_enableVertexAttribArray(0) {
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.vertexCount, GL11.GL_UNSIGNED_INT, 0)
            }
        }
