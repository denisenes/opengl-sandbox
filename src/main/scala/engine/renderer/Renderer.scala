package engine.renderer

import engine.GLExt.{GL_bindVertexArray, GL_enableVertexAttribArray}
import engine.models.TexturedModel
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.{GL11, GL13}

class Renderer:

    def prepare(): Unit =
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0.2, 0.2, 0.2, 1.0)

    def render(model: TexturedModel): Unit =
        GL_bindVertexArray(model.raw.vaoId) {
            GL_enableVertexAttribArray(0) {
                GL_enableVertexAttribArray(1) {
                    glActiveTexture(GL_TEXTURE0)
                    glBindTexture(GL_TEXTURE_2D, !model.texture.textureID)
                    glDrawElements(GL_TRIANGLES, model.raw.vertexCount, GL_UNSIGNED_INT, 0)
                }
            }
        }
