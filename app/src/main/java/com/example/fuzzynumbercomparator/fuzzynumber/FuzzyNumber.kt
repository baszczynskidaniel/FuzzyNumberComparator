package com.example.fuzzynumbercomparator.fuzzynumber

import android.graphics.PointF


interface FuzzyNumber {
    fun membership(x: Double): Double
    fun subArea(start: Double, end: Double): Double
    fun totalArea(): Double
    fun getCoordinates(): List<Double>
    fun getPoints(): List<Point<Double>>
    fun getStart(): Double
    fun getEnd(): Double
}

fun FuzzyNumber.isBiggerThanValue(value: Double) = value < this.getStart()
fun FuzzyNumber.isSmallerThanValue(value: Double) = value > this.getEnd()


