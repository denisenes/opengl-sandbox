package engine.renderer

import engine.Utils
import engine.Utils.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, GL15, GL20, GL30}

import java.nio.IntBuffer

object RawModel:

    class Loader extends AutoCloseable:
        private var VAOs: List[VaoID] = List()
        private var VBOs: List[VboID] = List()

        def loadToVAO(positions: Array[Float], indices: Array[Int]): RawModel =
            val vaoID = createVAO()

            GL_bindVertexArray(vaoID) {
                bindIndicesBuffer(indices)
                storeDataInAttributeList(0, positions)
            }

            new RawModel(vaoID, indices.length)

        private def createVAO(): Int =
            val vaoID = GL30.glGenVertexArrays()
            VAOs = VAOs :+ vaoID
            vaoID

        private def bindIndicesBuffer(indices: Array[Int]): Unit =
            val vboID: VboID = GL15.glGenBuffers()
            VBOs = VBOs :+ vboID

            GL_bindBuffer(vboID, GL15.GL_ELEMENT_ARRAY_BUFFER, false) {
                val buffer: IntBuffer = createIntBufferFromData(indices)
                GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
            } // do not unbind it from currently bound VAO!

        private def storeDataInAttributeList(attributeNum: Int, data: Array[Float]): Unit =
            val vboID = GL15.glGenBuffers()
            VBOs = VBOs :+ vboID

            GL_bindBuffer(vboID, GL15.GL_ARRAY_BUFFER, true) {
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

        override def close(): Unit =
            VAOs.foreach(vaoID => GL30.glDeleteVertexArrays(vaoID))
            VBOs.foreach(vboID => GL15.glDeleteBuffers(vboID))

    def withLoader(): Loader = new Loader()

class RawModel(val vaoId: VaoID, val vertexCount: Int)