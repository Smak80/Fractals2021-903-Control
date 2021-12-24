package ru.smak.ui

import org.kotlinmath.Complex
import ru. smak.math.fractals.Julia
import ru.smak.math.fractals.Mandelbrot
import ru.smak.ui.painting.CartesianPlane
import ru.smak.ui.painting.SelectablePanel
import ru.smak.ui.painting.fractals.FractalPainter
import ru.smak.ui.painting.fractals.colorizers
import java.awt.Color
import java.awt.Dimension
import java.awt.SystemColor.window
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.GroupLayout
import javax.swing.JFrame
import javax.swing.KeyStroke
import kotlin.random.Random


class SecondFrame : JFrame() {

    val fractalPanel: SelectablePanel

    val stat = mutableListOf(Pair(Pair(-1.0,1.0),Pair(-1.0,1.0)) )

    init{
        // defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(600, 400)
        val painter = FractalPainter(
            Julia(),
            CartesianPlane(-1.0, 1.0, -1.0, 1.0),
            colorizers[Random.nextInt(colorizers.size)])

        fractalPanel = SelectablePanel(painter).apply {
            background = Color.WHITE
            addSelectListener{
                with (painter.plane){
                    val xMin = xScr2Crt(it.x)
                    val yMin = yScr2Crt(it.y)
                    val xMax = xScr2Crt(it.x + it.width)
                    val yMax = yScr2Crt(it.y + it.height)
                    xSegment = Pair(xMin, xMax)
                    ySegment = Pair(yMin, yMax)

                    stat.add(Pair(Pair(xMin,xMax),Pair(yMin,yMax)))
                }
                repaint()
            }
            addMoveListener{
                with (painter.plane){
                    val xMin = xMin + xScr2Crt(0) -xScr2Crt(it.first)
                    val yMin = yMin + yScr2Crt(0) - yScr2Crt(it.second)
                    val xMax = xMax + xScr2Crt(0) -xScr2Crt(it.first)
                    val yMax = yMax + yScr2Crt(0) -yScr2Crt(it.second)
                    xSegment = Pair(xMin, xMax)
                    ySegment = Pair(yMin, yMax)

                    stat.add(Pair(Pair(xMin,xMax),Pair(yMin,yMax)))
                }
                repaint()
            }
        }

        fractalPanel.getInputMap().put(KeyStroke.getKeyStroke("control A"),"foo")
        fractalPanel.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {

            }
            override fun keyPressed(e: KeyEvent?) {
            }

            override fun keyReleased(e: KeyEvent?) {
                e?.let{
                    if(e.keyChar=='\u001A'){
                        if(stat.size!=1)
                            stat.removeAt(stat.size-1)
                        with (painter.plane){
                            xSegment = stat[stat.size-1].first
                            ySegment = stat[stat.size-1].second
                        }
                    }
                    repaint()
                }
            }
        })

        layout = GroupLayout(contentPane).apply {
            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(fractalPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGap(4)
            )
            setVerticalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(fractalPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGap(4)
            )
        }
    }

    //fun onSelectArea(r: Rectangle)
}