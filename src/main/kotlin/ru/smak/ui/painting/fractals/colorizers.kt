package ru.smak.ui.painting.fractals

import java.awt.Color
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.sin

val colorizers = mutableListOf<(Double)->Color>(
    ::bwFractal,
    ::pinkFractal,
    ::yellowFractal,
    ::redFractal,
    ::blueFractal,
)

fun bwFractal(x: Double) =
    Color(
        (1-x.coerceIn(0.0, 1.0)).toFloat(),
        (1-x.coerceIn(0.0, 1.0)).toFloat(),
        (1-x.coerceIn(0.0, 1.0)).toFloat()
    )

fun pinkFractal(x: Double): Color {
    if (x == 1.0) return Color.BLACK
    return Color(
        Math.abs(Math.cos(Math.log(12.0 * (1.0 - x)))).toFloat(),
        Math.abs(Math.sin(6.0 * (1.0 - x))).toFloat(),
        Math.abs(Math.sin(7.0 - 7.0 * x) * Math.cos(13.0 * x)).toFloat()
    )
}

fun yellowFractal(x: Double): Color {
    if (x == 1.0) return Color.BLACK
    return Color(
        Math.abs(Math.cos(6*x)).toFloat(),
        Math.abs(Math.cos(12*x)).toFloat(),
        Math.abs(Math.sin(7-7*x)).toFloat()
    )
}

fun redFractal(x: Double): Color {
    val r = 1F - abs(sin(17 + 6 * sin(20 * x)) * sin(7 + 2 * sin(x))).toFloat()
    val b = log2(1F + abs(sin(5 * sin(3 * x)) * cos(2 * sin(15* x)))).toFloat()
    val g = 1F - abs(cos(12 + 6 * sin(15 * x)) * cos(13 + 2 * sin(6 * x))).toFloat()
    return Color(r, g, b)
}

fun blueFractal(x: Double): Color {
    val r = (sin(2*sin(0.5*x))*sin(2*sin(0.3*x))).toFloat()
    val g = abs(sin(2*sin(15*x))*cos(2*sin(3*x))).toFloat()
    val b = (abs(cos(2*x))).toFloat()
    return Color(r, g, b)
}