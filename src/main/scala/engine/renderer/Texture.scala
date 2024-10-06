package engine.renderer

import engine.GLExt.{GL_bindTexture, TextureH}
import engine.LOG
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL11

import java.awt.image.BufferedImage
import java.io.FileInputStream
import java.nio.{ByteBuffer, ByteOrder, IntBuffer}
import javax.imageio.ImageIO

class Texture(val filepath: String):
    var width:  Int = 0
    var height: Int = 0
    val textureID: TextureH = load(filepath)

    private def imageToIntArray(image: BufferedImage): Array[Int] =
        val res: Array[Int] = new Array[Int](width * height)
        image.getRGB(0, 0, width, height, res, 0, width)

    private def load(path: String): TextureH =
        LOG(s"[Texture] Started loading texture from $path")
        val image = ImageIO.read(new FileInputStream(path))
        width  = image.getWidth
        height = image.getHeight

        val data: Array[Int] = imageToIntArray(image) map { p =>
            val a: Int = (p & 0xff000000) >> 24
            val r: Int = (p & 0x00ff0000) >> 16
            val g: Int = (p & 0x0000ff00) >> 8
            val b: Int = (p & 0x000000ff)

            a << 24 | b << 16 | g << 8 | r
        }

        val res: TextureH = TextureH.create()
        GL_bindTexture(GL_TEXTURE_2D, res) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

            val buffer: IntBuffer = ByteBuffer.allocateDirect(data.length * 4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(data)
                .flip()

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
        }

        LOG(s"[Texture] Texture from ${filepath} successfully loaded")
        res