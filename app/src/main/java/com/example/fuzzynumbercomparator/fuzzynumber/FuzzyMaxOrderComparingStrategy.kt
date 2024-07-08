package com.example.fuzzynumbercomparator.fuzzynumber

//https://hal.science/hal-00560708/document
class FuzzyMaxOrderComparingStrategy : ComparingStrategy {
    private val subAreas = SubAreaResults()

    override fun compare(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): Double {
        subAreas.setDefaultValues()
        return when {
            firstNumber.getStart() > secondNumber.getEnd() -> 1.0
            secondNumber.getStart() > firstNumber.getEnd() -> 0.0
            else -> calculateComparison(firstNumber, secondNumber)
        }
    }

    private fun calculateComparison(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): Double {
        val coordinates = LinearAlgebra.getIntersectionCoordinatesAndRange(firstNumber, secondNumber)
        for(i in coordinates.size - 1 downTo 1) {
            val middleCoordinate = (coordinates[i] + coordinates[i - 1]) / 2.0
            if(LinearAlgebra.areCoordinatesOnVerticalLine(middleCoordinate, coordinates[i]))
                continue

            val firstValue = firstNumber.membership(middleCoordinate)
            val secondValue = secondNumber.membership(middleCoordinate)

            updateSubArea(
                firstValue,
                secondValue,
                coordinates[i - 1],
                coordinates[i],
                firstNumber,
                secondNumber
            )
        }
        return calculateResult()
    }

    private fun updateSubArea(
        firstValue: Double,
        secondValue: Double,
        x1: Double,
        x2: Double,
        firstNumber: FuzzyNumber,
        secondNumber: FuzzyNumber) {
        when {
            firstValue == 0.0 && firstNumber.isBiggerThanValue(x1) -> {
                subAreas.b += secondNumber.subArea(x1, x2)
            }
            firstValue == 0.0 && firstNumber.isSmallerThanValue(x2) -> {
                subAreas.c += secondNumber.subArea(x1, x2)
            }
            secondValue == 0.0 && secondNumber.isBiggerThanValue(x1)-> {
                subAreas.c += firstNumber.subArea(x1, x2)
            }
            secondValue == 0.0 && secondNumber.isSmallerThanValue(x2) -> {
                subAreas.b += firstNumber.subArea(x1, x2)
            }
            firstValue > secondValue -> {
                subAreas.a += firstNumber.subArea(x1, x2) - secondNumber.subArea(x1, x2)
            }
            firstValue < secondValue -> {
                subAreas.d += secondNumber.subArea(x1, x2) - firstNumber.subArea(x1, x2)
            }
        }
    }

    private fun calculateResult(): Double {
        val result = (subAreas.d + subAreas.b) / subAreas.totalSubArea()
        return if (result.isNaN()) 0.5 else result
    }
}

data class SubAreaResults(
    var a: Double = 0.0,
    var b: Double = 0.0,
    var c: Double = 0.0,
    var d: Double = 0.0,
) {
    fun totalSubArea() = a + b + c + d
    fun setDefaultValues() {
        a = 0.0
        b = 0.0
        c = 0.0
        d = 0.0
    }
}



