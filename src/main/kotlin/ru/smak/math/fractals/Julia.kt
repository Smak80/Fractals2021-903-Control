package ru.smak.math.fractals
import org.kotlinmath.Complex
import ru.smak.math.mod2

class Julia: AlgebraicFractal {
    /**
     * Количество итераций, которые необходимы для проверки принедлежности
     * точки множеству Мандельброта
     */
    var maxIterations = 200

    /**
     * Квадрат радиуса принадлежности
     * @see R
     */
    private var R2: Double = 4.0
    //public  var t=org.kotlinmath.DefaultComplex(0.0,0.0)
    companion object {
        var t=org.kotlinmath.DefaultComplex(0.0,0.0)
    }
//
    /**
     * Радиус окрестности, где должна оказаться точка после maxIterations итераций,
     * чтобы считаться принадлежащей множеству Мандельброта
     * @see maxIterations
     */
    var R: Double = 2.0
        set(value) {
            field = value
            R2 = value * value
        }

    /**
     * Определяет тип возвращаемого значения для точек из множества Мандельброта.
     * true  - для точек из множества будут возвращаться различные значения,
     *         зависящие от модуля комплексного числа z, полученного после maxIterations итераций.
     * false - для точек из множества будет выдаваться значение 1F
     * @see maxIterations
     */
    var colorizedSet: Boolean = false

    /**
     * Метод определения принадлежности точки множеству Мандельброта
     * @param c точка комплексной плоскости, для которой определяется принадлежность
     * множеству Мандельброта
     * @return значение доли успешно проведенных итераций,
     * в течение которых точка могла бы считаться принадлежащей множеству,
     * либо значение 1F для точек, принадлежащий множеству в случае colorizedSet = false,
     * либо величина модуля полученного в ходе итераций комплексного числа в случае colorizeDSet = true
     * @see colorizedSet
     */
    override fun isInSet(c: Complex): Double {

        //var t = org.kotlinmath.DefaultComplex(0.0, 0.0)
        var z= c
        for(i in 0 until maxIterations){
            z = z * z + t
            if (z.mod2 > R2) return i.toDouble() / maxIterations
        }
        return if (colorizedSet) z.mod else 1.0
    }
}