package engine.renderer

import engine.GLExt.{GL_bindVertexArray, GL_enableVertexAttribArray, GL_withShader}
import engine.{LOG, Utils}
import engine.entities.Entity
import engine.shaders.StaticShader
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.{GL11, GL13}

import java.lang.Math

class Renderer(val dm: DisplayManager, val staticShader: StaticShader):

    private val FOV        = 70
    private val NEAR_PLANE = 0.1f
    private val FAR_PLANE  = 1000

    private val projectionMatrix =
        Matrix4f().perspective(
            Math.toRadians(FOV).toFloat,
            dm.getWindowWidth().toFloat / dm.getWindowHeight().toFloat,
            NEAR_PLANE, FAR_PLANE
        )

    GL_withShader(staticShader) {
        staticShader.loadProjectionMatrix(projectionMatrix)
    }

    def prepare(): Unit =
        LOG("[Renderer] prepare")
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0.2, 0.2, 0.2, 1.0)

    def render(entity: Entity): Unit =
        LOG("[Renderer] render start")
        GL_withShader(staticShader) {
            val model = entity.model

            val transMtx = Utils.Math.createTransMtx(
                entity.position,
                entity.rotX, entity.rotY, entity.rotZ,
                entity.scale)

            GL_bindVertexArray(model.raw.vaoId) {
                GL_enableVertexAttribArray(0) {
                    GL_enableVertexAttribArray(1) {
                        staticShader.loadTransformationMatrix(transMtx)
                        glActiveTexture(GL_TEXTURE0)
                        LOG("[Renderer] set active texture")
                        glBindTexture(GL_TEXTURE_2D, !model.texture.textureID)
                        LOG("[Renderer] binded texture")
                        glDrawElements(GL_TRIANGLES, model.raw.vertexCount, GL_UNSIGNED_INT, 0)
                        LOG("[Renderer] drew texture")
                    }
                }
            }
        }
        LOG("[Renderer] render end")
