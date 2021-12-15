package ru.smak.ui.painting

import ru.smak.ui.GraphicsPanel
import java.awt.*
import java.awt.event.ComponentEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.math.min

class SelectablePanel(vararg painters: Painter) : GraphicsPanel(*painters){

    private var pt1: Point? = null
    private var pt2: Point? = null
    private val originalImage: BufferedImage? = null

    private val stat = mutableListOf(Rectangle(0,0,width,height))

    private val selectListener: MutableList<(Rectangle)->Unit> = mutableListOf()

    fun addSelectListener(l: (Rectangle)->Unit){
        selectListener.add(l)
    }

    fun removeSelectListener(l: (Rectangle)->Unit){
        selectListener.remove(l)
    }

    fun getImage(): BufferedImage? {

        return originalImage
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
                        val r = Rectangle(min(p1.x,p2.x),min(p1.y,p2.y), abs(p2.x - p1.x),abs(p2.y-p1.y))

                        stat.add(r)

                        selectListener.forEach { it(r) }
                    }
                }
                pt1 = null
                pt2 = null
            }

            override fun mouseClicked(e: MouseEvent?) {
                super.mouseClicked(e)
                e?.let {
                    stat.removeAt(stat.size-1)
                    selectListener.forEach { it(stat[stat.size-1]) }
                }
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