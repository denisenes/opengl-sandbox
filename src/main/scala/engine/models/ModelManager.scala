package engine.models

import engine.GLExt.{TextureH, VaoH, VboH}
import engine.Utils
import engine.renderer.Texture

object ModelManager extends AutoCloseable:
    private var VAOs:     List[VaoH]     = List()
    private var VBOs:     List[VboH]     = List()
    private var textures: List[TextureH] = List()

    def loadRawModel(positions: Array[Float], indices: Array[Int]): RawModel =
        val model = RawModel(positions, indices)
        register(model.vaoId)
        register(model.indicesVboId)
        register(model.verticesVboId)
        model

    def loadTexture(filename: String): Texture =
        val texture = new Texture(Utils.Paths.TEXTURES_DIR + filename)
        register(texture.textureID)
        texture

    private[models] def register(obj: AnyVal): Unit =
        obj match
            case vao: VaoH         => VAOs = VAOs :+ vao
            case vbo: VboH         => VBOs = VBOs :+ vbo
            case texture: TextureH => textures = textures :+ texture
            case _                 => throw new IllegalArgumentException()

    override def close(): Unit =
        VAOs.foreach(vaoID => vaoID.destroy())
        VBOs.foreach(vboID => vboID.destroy())
        textures.foreach(texture => texture.destroy())