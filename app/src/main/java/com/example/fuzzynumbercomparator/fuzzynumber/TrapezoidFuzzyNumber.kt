package com.example.fuzzynumbercomparator.fuzzynumber

import android.graphics.PointF

/**
 * @param a start of fuzzy number must be smaller than c
 * @param b core of fuzzy number it must be between a and c inclusive
 * @param c end of fuzzy number must be greater than a
 */
class TrapezoidFuzzyNumber(
    val a: Float,
    val b: Float,
    val c: Float,
    val d: Float
): FuzzyNumber {


    init {
        if(a > b)
            throw IllegalArgumentException("a must be smaller or equal b")
        if(b > c)
            throw IllegalArgumentException("b must be smaller or equal c")
        if(c > d)
            throw IllegalArgumentException("c must be smaller or equal d")
    }

    override fun getCoordinates(): List<Float> = listOf(a, b, c, d).distinct()
    override fun totalArea(): Float = ((d - a) + (c - b)) / 2f

    override fun getPoints(): List<PointF> {
        return listOf(PointF(a, 0f), PointF(b, 1f), PointF(c, 1f), PointF(d, 0f))
    }

    override fun getStart(): Float = a

    override fun getEnd(): Float = d

    override fun membership(x: Float): Float {
        return when (x) {
            in b .. c -> 1.0f
            in a .. b -> {
                1.0f / (b - a) * x + a / (a - b)
            }
            in c .. d -> {
                1.0f / (c - d) * x + d / (d - c)
            }
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

        currentStart = maxOf(c, start)
        currentEnd = minOf(d, end)
        if(currentEnd >= c){
            area += (membership(currentEnd) + membership(currentStart)) * (currentEnd - currentStart) / 2.0f
        }
        return area
    }
}