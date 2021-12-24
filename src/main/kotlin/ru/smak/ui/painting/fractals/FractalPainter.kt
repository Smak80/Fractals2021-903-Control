package ru.smak.ui.painting.fractals

import org.kotlinmath.complex
import ru.smak.math.fractals.AlgebraicFractal
import ru.smak.ui.painting.CartesianPlane
import ru.smak.ui.painting.Painter
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.util.concurrent.Callable
import java.util.concurrent.Executors


class FractalPainter(
    val fractal: AlgebraicFractal,
    val plane: CartesianPlane,
    var colorFunction: (Double)->Color
) : Painter {

    override var size: Dimension
        get() = plane.pixelSize
        set(value) {
            plane.pixelSize = value
        }
//
    private val threadCount = 16*4
    private val taskPerThreadCount = 5
    private var fracService = Executors.newFixedThreadPool(threadCount)
    private val rSize = 25
    private val nSize = 80
    private val list = mutableListOf<BufferedImage>()


    val image: BufferedImage
    get() {
        val image = BufferedImage(plane.width, plane.height+rSize, BufferedImage.TYPE_INT_RGB)
        val g1 = image.graphics
        for (i in 0 until  list.size) {
            g1.drawImage(list[i],  list[0].width*i,0, list[i].width,  list[i].height, null)
        }
        g1.color = Color.BLACK
        g1.fillRect(0, plane.height, list[0].width*list.size, rSize)
        g1.color= Color.WHITE
        g1.drawString( "xMin="+String.format("%.3f", plane.xMin)  , 10 , plane.height+rSize/2)
        g1.drawString( "xMax="+String.format("%.3f", plane.xMax)  , 10 + nSize , plane.height+rSize/2)
        g1.drawString( "yMin="+String.format("%.3f", plane.yMin)  , 10 + 2*nSize , plane.height+rSize/2)
        g1.drawString( "yMax="+String.format("%.3f", plane.yMax)  , 10 + 3*nSize , plane.height+rSize/2)
        return image
    }


    override fun paint(g: Graphics) {
        with(plane){
            if (list.size!=0){
                list.clear()
            }
            if (width <= 0 || height <= 0) return
            if (!(fracService.isShutdown || fracService.isTerminated)) {
                fracService.shutdown()
                fracService = Executors.newFixedThreadPool(threadCount)
            }
            val cnt = width / (threadCount * taskPerThreadCount)
            val wsz = width / cnt
            List(cnt){ i ->
                fracService.submit(Callable {
                    val w = wsz + if (i + 1 == cnt) width % cnt else 0
                    val img = BufferedImage(w, height, BufferedImage.TYPE_INT_RGB)
                    for (k in 0 until w) {
                        for (j in 0..height) {
                            with (img.graphics) {
                                color = colorFunction(
                                    fractal.isInSet(
                                        complex(
                                            xScr2Crt(i * w + k),
                                            yScr2Crt(j)
                                        )
                                    )
                                )
                                fillRect(k, j, 1, 1)
                            }
                        }
                    }
                    //list.add(img)
                    img
                })
            }.forEachIndexed { i, v ->
                val img = v.get()
                list.add(img)
                g.drawImage(img, i * wsz, 0, null)
            }

        }
    }

}