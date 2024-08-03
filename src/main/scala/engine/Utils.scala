package engine

import org.lwjgl.opengl.{GL15, GL20, GL30}

object Utils:

    type WindowHandle = Long
    type VaoID = Int
    type VboID = Int
    type ShaderID = Int
    type ProgramID = Int

    def GL_bindBuffer(vboID: VboID, target: Int, unbind: Boolean)(f: => Unit): Unit =
        GL15.glBindBuffer(target, vboID)
        f
        if unbind then
            GL15.glBindBuffer(target, 0)

    def GL_bindVertexArray(vaoID: VaoID)(f: => Unit): Unit =
        GL30.glBindVertexArray(vaoID)
        f
        GL30.glBindVertexArray(0)

    def GL_enableVertexAttribArray(attrNum: Int)(f: => Unit): Unit =
        GL20.glEnableVertexAttribArray(0)
        f
        GL20.glDisableVertexAttribArray(0)

class Utils
