package engine.models

import engine.GLExt.*
import engine.Utils
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, GL15, GL20, GL30}

import java.nio.IntBuffer

class RawModel(private val vertices: Array[Float],
               private val textureCoords: Array[Float],
               private val indices: Array[Int]):
    val vertexCount: Int    = indices.length

    val vaoId: VaoH       = VaoH.create()
    var vboIds:  List[VboH] = List()

    GL_bindVertexArray(vaoId) {
        bindIndicesBuffer(indices)
        initNewVBOAndPopulate(0, vertices)
        initNewVBOAndPopulate(1, textureCoords, dimensions = 2)
    }

    private def bindIndicesBuffer(indices: Array[Int]): Unit =
        val indicesVboId = VboH.create()
        GL_bindBuffer(indicesVboId, GL15.GL_ELEMENT_ARRAY_BUFFER, false) {
            val buffer: IntBuffer = createIntBufferFromData(indices)
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
        } // do not unbind it from currently bound VAO!
        vboIds = vboIds :+ indicesVboId

    private def initNewVBOAndPopulate(attributeNum: Int, data: Array[Float], dimensions: Int = 3): Unit =
        val vboId = VboH.create()
        GL_bindBuffer(vboId, GL15.GL_ARRAY_BUFFER, true) {
            GL15.glBufferData(
                GL15.GL_ARRAY_BUFFER,
                createFloatBufferFromData(data),
                GL15.GL_STATIC_DRAW
            )
            GL20.glVertexAttribPointer(attributeNum, dimensions, GL11.GL_FLOAT, false, 0, 0)
        }
        vboIds = vboIds :+ vboId

    private def createIntBufferFromData(data: Array[Int]) = BufferUtils
        .createIntBuffer(data.length)
        .put(data)
        .flip()

    private def createFloatBufferFromData(data: Array[Float]) = BufferUtils
        .createFloatBuffer(data.length)
        .put(data)
        .flip()