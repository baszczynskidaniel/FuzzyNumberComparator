package com.example.fuzzynumbercomparator.fuzzynumber

import android.graphics.PointF


interface FuzzyNumber {
    fun membership(x: Float): Float
    fun subArea(start: Float, end: Float): Float
    fun totalArea(): Float

    fun getCoordinates(): List<Float>
    fun getPoints(): List<PointF>
    fun getStart(): Float
    fun getEnd(): Float
}

fun FuzzyNumber.isBiggerThanValue(value: Float) = value < this.getStart()
fun FuzzyNumber.isSmallerThanValue(value: Float) = value > this.getEnd()


