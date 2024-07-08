package com.example.fuzzynumbercomparator.fuzzynumber

import android.graphics.PointF

/**
 * @param a start of fuzzy number must be smaller than c
 * @param b core of fuzzy number it must be between a and c inclusive
 * @param c end of fuzzy number must be greater than a
 */
class TriangleFuzzyNumber(
    val a: Float,
    val b: Float,
    val c: Float,
): FuzzyNumber {

    init {
       validateParameters()
    }

    private fun validateParameters() {
        if (a > b || a >= c || b > c) {
            throw IllegalArgumentException("a must be smaller than c, b cannot be greater than c and smaller than a")
        }
    }


    override fun getCoordinates(): List<Float> = listOf(a, b, c).distinct()
    override fun totalArea(): Float = (c - a) * 0.5f

    override fun getPoints(): List<PointF> {
        return listOf(PointF(a, 0f), PointF(b, 1f), PointF(c, 0f))
    }

    override fun membership(x: Float): Float {
        return when (x) {
            b -> 1.0f
            in a .. b -> 1.0f / (b - a) * x + a / (a - b)
            in b .. c -> 1.0f / (b - c) * x + c / (c - b)
            else -> 0.0f
        }
    }

    override fun subArea(start: Float, end: Float): Float {
        if(start >= end || start > c || end < a)
            return 0.0f

        var area = 0.0f
        var currentEnd = minOf(b, end)
        var currentStart = maxOf(a, start)

        if(currentStart <= b){
            area += (membership(currentEnd) + membership(currentStart)) * (currentEnd - currentStart) / 2.0f
        }
        currentStart = maxOf(b, start)
        currentEnd = minOf(c, end)
        if(currentEnd >= b){
            area += (membership(currentEnd) + membership(currentStart)) * (currentEnd - currentStart) / 2.0f
        }
        return area
    }



    override fun getStart(): Float = a
    override fun getEnd(): Float = c
}