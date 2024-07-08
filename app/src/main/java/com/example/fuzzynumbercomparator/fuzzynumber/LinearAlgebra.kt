package com.example.fuzzynumbercomparator.fuzzynumber

import android.graphics.PointF
import kotlin.math.min
import kotlin.math.sqrt


class LinearAlgebra {
    companion object {
        fun getLineSegments(number: FuzzyNumber): List<Pair<Point<Double>, Point<Double>>> {
            val pointCoordinates = mutableListOf<Pair<Point<Double>, Point<Double>>>()
            val points = number.getPoints()
            for(i in 0 until points.size - 1) {
                pointCoordinates.add(Pair(points[i], points[i + 1]))
            }
            return pointCoordinates
        }

        fun areTwoLinesParallel(firstLine: Pair<Point<Double>, Point<Double>>, secondLine: Pair<Point<Double>, Point<Double>>): Boolean {
            val m1 = (firstLine.second.y - firstLine.first.y) / (firstLine.second.x - firstLine.first.x)
            val m2 = (secondLine.second.y - secondLine.first.y) / (secondLine.second.x - secondLine.first.x)
            return m1 == m2
        }

        private fun areCoordinatesOnVerticalLine(firstPoint: Point<Double>, secondPoint: Point<Double>) = firstPoint.x == secondPoint.x

        private fun areCoordinatesOnVerticalLine(points: Pair<Point<Double>, Point<Double>>) = areCoordinatesOnVerticalLine(points.first, points.second)

        private fun getLinearFunctionValue(x: Double, points: Pair<Point<Double>, Point<Double>>): Double {
            val x1 = points.first.x
            val x2 = points.second.x
            val y1 = points.first.y
            val y2 = points.second.y
            return (y1 - y2) / (x1 - x2) * x + y1 - (y1 - y2) * x1 / (x1 - x2)
        }

        fun getIntersectionPointWithVerticalLine(
            points: Pair<Point<Double>, Point<Double>>,
            verticalLineX: Double
        ): Point<Double> {
            val y = getLinearFunctionValue(verticalLineX, points)
            return Point<Double>(verticalLineX, y)
        }

        fun getIntersectionPointInLineSegmentOrNull(line1: Pair<Point<Double>, Point<Double>>, line2: Pair<Point<Double>, Point<Double>>): Point<Double>? {

            if(areTwoLinesParallel(line1, line2)) {
                return null
            }

            if(areCoordinatesOnVerticalLine(line1)) {

               return getIntersectionPointWithVerticalLine(line2, line1.first.x)
            }

            if(areCoordinatesOnVerticalLine(line2)) {

                return getIntersectionPointWithVerticalLine(line1, line2.first.x)
            }

            val x1 = line1.first.x
            val x2 = line1.second.x
            val x3 = line2.first.x
            val x4 = line2.second.x
            val y1 = line1.first.y
            val y2 = line1.second.y
            val y3 = line2.first.y
            val y4 = line2.second.y

            val t = ((x1 - x3)*(y3 - y4) - (y1 - y3) * (x3 - x4)) / ((x1 - x2)*(y3 - y4) - (y1 - y2) * (x3 - x4))
            val u = -((x1 - x2)*(y1 - y3) - (y1 - y2) * (x1 - x3)) / ((x1 - x2)*(y3 - y4) - (y1 - y2) * (x3 - x4))


            val x = x1 + t*(x2 - x1)
            val y = y1 + t*(y2 - y1)

            if(t in 0.0..1.0 && u in 0.0..1.0)
                return Point<Double>(x, y)
            else
                return null
        }

        fun areCoordinatesOnVerticalLine(x1: Double, x2: Double) = x1 == x2

        fun getIntersectionPoints(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): List<Point<Double>> {
            val intersectionPoints = mutableSetOf<Point<Double>>()
            if(firstNumber.getStart() > secondNumber.getEnd() || secondNumber.getStart() > firstNumber.getEnd())
                return emptyList()
            intersectionPoints.add(
                Point<Double>(
                    maxOf(firstNumber.getStart(), secondNumber.getStart()),
                    0.0
                )
            )
            intersectionPoints.add(Point<Double>(min(firstNumber.getEnd(), secondNumber.getEnd()), 0.0))
            val firstNumberLineSegments = getLineSegments(firstNumber)
            val secondNumberLineSegments = getLineSegments(secondNumber)

            for(firstLine in firstNumberLineSegments) {
                for(secondLine in secondNumberLineSegments) {
                    val point = getIntersectionPointInLineSegmentOrNull(firstLine, secondLine)
                    if(point != null && isPointInLineSegment(point, firstLine))
                        intersectionPoints.add(point)
                }
            }
            val list = intersectionPoints.toMutableList().sortedWith(compareByDescending<Point<Double>> { it.x }.thenByDescending { it.y }).toMutableList()
            // points in correct order to be draw

            return list
        }



        fun isPointInLineSegment(point: Point<Double>, lineSegment: Pair<Point<Double>, Point<Double>>): Boolean {

            return isPointInLineSegment(point, lineSegment.first, lineSegment.second)
        }

        fun isPointInLineSegment(point: Point<Double>, start: Point<Double>, end: Point<Double>): Boolean {

            val floatingPointErrorCorrect = 0.00001
            return distanceBetweenPoints(start, end) < distanceBetweenPoints(point, start) + distanceBetweenPoints(point, end) + floatingPointErrorCorrect &&
                    distanceBetweenPoints(start, end) > distanceBetweenPoints(point, start) + distanceBetweenPoints(point, end) - floatingPointErrorCorrect
        }

        fun distanceBetweenPoints(firstPoint: Point<Double>, secondPoint: Point<Double>): Double {
            return sqrt((firstPoint.x - secondPoint.x) * (firstPoint.x - secondPoint.x) + (firstPoint.y - secondPoint.y) * (firstPoint.y - secondPoint.y))
        }

        fun getIntersectionPointsAndRange(number1: FuzzyNumber, number2: FuzzyNumber): List<Point<Double>> {
            val points = getNumberRangePoints(number1, number2).toMutableSet()
            points += getIntersectionPoints(number1, number2)
            return points.toList().sortedWith(compareBy({it.x}, {it.y}))
        }

        fun getIntersectionCoordinatesAndRange(number1: FuzzyNumber, number2: FuzzyNumber): List<Double> {
            val points = getNumberRangePoints(number1, number2).toMutableSet()
            points += getIntersectionPoints(number1, number2)
            return points.map { it.x }.sorted()
        }
        fun getNumberRangePoints(number1: FuzzyNumber, number2: FuzzyNumber): List<Point<Double>> {
            val range = mutableSetOf<Point<Double>>()
            val start = minOf(number1.getCoordinates().first(), number2.getCoordinates().first())
            range.add(Point<Double>(start, 0.0))
            val end = maxOf(number1.getCoordinates().last(), number2.getCoordinates().last())
            range.add(Point<Double>(end, 0.0))
            return range.toMutableList()
        }
    }
}