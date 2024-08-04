package engine.models

import engine.GLExt.*
import engine.Utils
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, GL15, GL20, GL30}

import java.nio.IntBuffer

class RawModel(private val vertices: Array[Float], private val indices: Array[Int]):
    val vertexCount: Int    = indices.length

    val vaoId: VaoH         = VaoH.create()
    val verticesVboId: VboH = VboH.create()
    val indicesVboId:  VboH = VboH.create()

    GL_bindVertexArray(vaoId) {
        bindIndicesBuffer(indices)
        storeDataInAttributeList(0, vertices)
    }

    private def bindIndicesBuffer(indices: Array[Int]): Unit =
        GL_bindBuffer(indicesVboId, GL15.GL_ELEMENT_ARRAY_BUFFER, false) {
            val buffer: IntBuffer = createIntBufferFromData(indices)
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
        } // do not unbind it from currently bound VAO!

    private def storeDataInAttributeList(attributeNum: Int, data: Array[Float]): Unit =
        GL_bindBuffer(verticesVboId, GL15.GL_ARRAY_BUFFER, true) {
            GL15.glBufferData(
                GL15.GL_ARRAY_BUFFER,
                createFloatBufferFromData(data),
                GL15.GL_STATIC_DRAW
            )
            GL20.glVertexAttribPointer(attributeNum, 3, GL11.GL_FLOAT, false, 0, 0)
        }

    private def createIntBufferFromData(data: Array[Int]) = BufferUtils
        .createIntBuffer(data.length)
        .put(data)
        .flip()

    private def createFloatBufferFromData(data: Array[Float]) = BufferUtils
        .createFloatBuffer(data.length)
        .put(data)
        .flip()