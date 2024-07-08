package com.example.fuzzynumbercomparator.fuzzynumber

//https://hal.science/hal-00560708/document
class FuzzyMaxOrderComparingStrategy : ComparingStrategy {
    private val subAreas = SubAreaResults()

    override fun compare(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): Float {
        subAreas.setDefaultValues()
        return when {
            firstNumber.getStart() > secondNumber.getEnd() -> 1f
            secondNumber.getStart() > firstNumber.getEnd() -> 0f
            else -> calculateComparison(firstNumber, secondNumber)
        }
    }

    private fun calculateComparison(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): Float {
        val coordinates = LinearAlgebra.getIntersectionCoordinatesAndRange(firstNumber, secondNumber)
        for(i in coordinates.size - 1 downTo 1) {
            val middleCoordinate = (coordinates[i] + coordinates[i - 1]) / 2f
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
        firstValue: Float,
        secondValue: Float,
        x1: Float,
        x2: Float,
        firstNumber: FuzzyNumber,
        secondNumber: FuzzyNumber) {
        when {
            firstValue == 0f && firstNumber.isBiggerThanValue(x1) -> {
                subAreas.b += secondNumber.subArea(x1, x2)
            }
            firstValue == 0f && firstNumber.isSmallerThanValue(x2) -> {
                subAreas.c += secondNumber.subArea(x1, x2)
            }
            secondValue == 0f && secondNumber.isBiggerThanValue(x1)-> {
                subAreas.c += firstNumber.subArea(x1, x2)
            }
            secondValue == 0f && secondNumber.isSmallerThanValue(x2) -> {
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

    private fun calculateResult(): Float {
        val result = (subAreas.d + subAreas.b) / subAreas.totalSubArea()
        return if (result.isNaN()) 0.5f else result
    }
}

data class SubAreaResults(
    var a: Float = 0f,
    var b: Float = 0f,
    var c: Float = 0f,
    var d: Float = 0f,
) {
    fun totalSubArea() = a + b + c + d
    fun setDefaultValues() {
        a = 0f
        b = 0f
        c = 0f
        d = 0f
    }
}



