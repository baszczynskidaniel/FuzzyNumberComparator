package com.example.fuzzynumbercomparator.fuzzynumber

import android.graphics.PointF

/**
 * @param a start of fuzzy number must be smaller than c
 * @param b core of fuzzy number it must be between a and c inclusive
 * @param c end of fuzzy number must be greater than a
 */
class TrapezoidFuzzyNumber(
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double
): FuzzyNumber {
    init {
        if(a > b)
            throw IllegalArgumentException("a must be smaller or equal b")
        if(b > c)
            throw IllegalArgumentException("b must be smaller or equal c")
        if(c > d)
            throw IllegalArgumentException("c must be smaller or equal d")
    }

    override fun getCoordinates(): List<Double> = listOf(a, b, c, d).distinct()
    override fun totalArea(): Double = ((d - a) + (c - b)) / 2f

    override fun getPoints(): List<Point<Double>> {
        return listOf(Point(a, 0.0), Point(b, 1.0), Point(c, 1.0), Point(d, 0.0))
    }

    override fun getStart(): Double = a

    override fun getEnd(): Double = d

    override fun membership(x: Double): Double {
        return when (x) {
            in b .. c -> 1.0
            in a .. b -> {
                1.0 / (b - a) * x + a / (a - b)
            }
            in c .. d -> {
                1.0 / (c - d) * x + d / (d - c)
            }
            else -> 0.0
        }
    }



    override fun subArea(start: Double, end: Double): Double {
        if(start >= end || start > d || end < a)
            return 0.0

        var area = 0.0
        var currentEnd = minOf(b, end)
        var currentStart = maxOf(a, start)

        if(currentStart <= b){
            area += LinearAlgebra.trapezoidArea(
                a = membership(currentEnd),
                b = membership(currentStart),
                h = currentEnd - currentStart)
        }

        currentEnd = minOf(c, end)
        currentStart = maxOf(b, start)

        if(currentEnd >= b){
            area += LinearAlgebra.trapezoidArea(
                a = membership(currentEnd),
                b = membership(currentStart),
                h = currentEnd - currentStart)
        }
        currentEnd = minOf(d, end)
        currentStart = maxOf(c, start)

        if(currentEnd >= c){
            area += LinearAlgebra.trapezoidArea(
                a = membership(currentEnd),
                b = membership(currentStart),
                h = currentEnd - currentStart)
        }
        return area
    }
}

fun main() {
    val t = TrapezoidFuzzyNumber(1.0, 2.0, 3.0, 4.0)
    val sum = t.subArea(3.5, 4.0)
    println(sum)
}