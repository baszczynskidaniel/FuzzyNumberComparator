package com.example.fuzzynumbercomparator.fuzzynumber

import android.graphics.PointF

/**
 * @param a start of fuzzy number must be smaller than c
 * @param b core of fuzzy number it must be between a and c inclusive
 * @param c end of fuzzy number must be greater than a
 */
class TriangleFuzzyNumber(
    val a: Double,
    val b: Double,
    val c: Double,
): FuzzyNumber {

    init {
       validateParameters()
    }

    private fun validateParameters() {
        if (a > b || a >= c || b > c) {
            throw IllegalArgumentException("a must be smaller than c, b cannot be greater than c and smaller than a")
        }
    }


    override fun getCoordinates(): List<Double> = listOf(a, b, c).distinct()
    override fun totalArea(): Double = (c - a) * 0.5

    override fun getPoints(): List<Point<Double>> {
        return listOf(Point(a, 0.0), Point(b, 1.0), Point(c, 0.0))
    }

    override fun membership(x: Double): Double {
        return when (x) {
            b -> 1.0
            in a .. b -> 1.0 / (b - a) * x + a / (a - b)
            in b .. c -> 1.0 / (b - c) * x + c / (c - b)
            else -> 0.0
        }
    }

    override fun subArea(start: Double, end: Double): Double {
        if(start >= end || start > c || end < a)
            return 0.0

        var area = 0.0
        var currentEnd = minOf(b, end)
        var currentStart = maxOf(a, start)

        if(currentStart <= b){

            area += (membership(currentEnd) + membership(currentStart)) * (currentEnd - currentStart) / 2.0
        }
        currentStart = maxOf(b, start)
        currentEnd = minOf(c, end)
        if(currentEnd >= b){
            area += (membership(currentEnd) + membership(currentStart)) * (currentEnd - currentStart) / 2.0
        }
        return area
    }



    override fun getStart(): Double = a
    override fun getEnd(): Double = c
}