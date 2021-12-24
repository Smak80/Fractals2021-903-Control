package ru.smak.ui.painting

import ru.smak.math.fractals.Mandelbrot
import ru.smak.ui.GraphicsPanel
import ru.smak.ui.painting.fractals.FractalPainter
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.*
import kotlin.math.abs
import kotlin.math.min

class SelectablePanel(vararg painters: Painter) : GraphicsPanel(*painters){

    private var pt1: Point? = null
    private var pt2: Point? = null

    private val selectListener: MutableList<(Rectangle)->Unit> = mutableListOf()
    private val moveListener: MutableList<(Pair<Int, Int>)->Unit> = mutableListOf()


    fun addSelectListener(l: (Rectangle)->Unit){
        selectListener.add(l)
    }

    fun addMoveListener(l: (Pair<Int, Int>)->Unit){
        moveListener.add(l)
    }

    fun removeSelectListener(l: (Rectangle)->Unit){
        selectListener.remove(l)
    }


    init {
        addMouseListener(object : MouseAdapter(){

            override fun mousePressed(e: MouseEvent?) {
                graphics.apply {
                    setXORMode(Color.WHITE)
                    fillRect(2*width, 0, 1, 1)
                    setPaintMode()
                }
                pt1 = e?.point
            }

            override fun mouseReleased(e: MouseEvent?) {
                pt1?.let { p1 ->
                    pt2?.let { p2->
                        e?.let {
                            if(e.button == 3) {
                                val r = Pair((p2.x - p1.x),(p2.y-p1.y))
                                moveListener.forEach { it(r) }
                            }
                            if(e.button == 1) {
                                val r = Rectangle(min(p1.x,p2.x),min(p1.y,p2.y), abs(p2.x - p1.x),abs(p2.y-p1.y))
                                selectListener.forEach { it(r) }
                            }
                        }
                    }
                }
                pt1 = null
                pt2 = null
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter(){
            override fun mouseDragged(e: MouseEvent?) {
                with (graphics){
                    setXORMode(Color.WHITE)
                    pt1?.let { pt ->
                        pt2?.let{ pt2 ->
                            drawRect(min(pt.x,pt2.x),min(pt.y,pt2.y), abs(pt2.x - pt.x),abs(pt2.y-pt.y))
                        }
                        pt2 = e?.point
                        e?.let { e ->
                            drawRect(min(pt.x,e.x), min(pt.y,e.y), abs(e.x - pt.x), abs(e.y-pt.y))
                        }
                    }
                    setPaintMode()
                }
            }
        })
    }
}