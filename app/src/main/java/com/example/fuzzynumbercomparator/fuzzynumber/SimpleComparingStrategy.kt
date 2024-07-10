package com.example.fuzzynumbercomparator.fuzzynumber

class SimpleComparingStrategy(
    val alpha: Float = 2.0f,
    val beta: Float = 1.0f,
) : ComparingStrategy {
    override fun compare(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): Double {

        if(firstNumber.getStart() > secondNumber.getEnd())
            return 1.0
        if(firstNumber.getEnd() < secondNumber.getStart())
            return 0.0

        val coordinates = LinearAlgebra.getIntersectionCoordinatesAndRange(firstNumber, secondNumber)
        var totalAreaFirstNumberGreater = 0.0
        var totalAreaSecondNumberGreater = 0.0

        for (i in 0 until coordinates.size - 1) {

            val start = coordinates[i]
            val end = coordinates[i + 1]
            val firstSubArea = firstNumber.subArea(start, end)
            val secondSubArea = secondNumber.subArea(start, end)

            when {
                secondSubArea == 0.0 && secondNumber.isBiggerThanValue(start) -> {
                    totalAreaSecondNumberGreater += firstSubArea * alpha
                }
                firstSubArea == 0.0 && !firstNumber.isBiggerThanValue(start) -> {
                    totalAreaSecondNumberGreater += secondSubArea * alpha
                }

                secondSubArea == 0.0 && !secondNumber.isBiggerThanValue(start) -> {
                    totalAreaFirstNumberGreater += firstSubArea * alpha
                }
                firstSubArea == 0.0 && firstNumber.isBiggerThanValue(start) -> {
                    totalAreaFirstNumberGreater += secondSubArea * alpha
                }

                firstSubArea > secondSubArea -> {
                    totalAreaFirstNumberGreater += (firstSubArea - secondSubArea) * alpha + secondSubArea * beta
                    totalAreaSecondNumberGreater += secondSubArea * beta
                }

                firstSubArea < secondSubArea -> {
                    totalAreaSecondNumberGreater += (secondSubArea - firstSubArea) * alpha + firstSubArea * beta
                    totalAreaFirstNumberGreater += firstSubArea * beta
                }
                // 1 - 0.5     2 - 0.5

                firstSubArea == secondSubArea -> {
                    totalAreaSecondNumberGreater += firstSubArea * beta
                    totalAreaFirstNumberGreater += firstSubArea * beta
                }
            }
        }
        return totalAreaFirstNumberGreater / (totalAreaFirstNumberGreater + totalAreaSecondNumberGreater)
    }

    private fun calculateComparisonResult(
        totalAreaFirstNumberGreater: Float,
        totalEqualArea: Float,
        totalArea: Float
    ): Float {

        return (totalAreaFirstNumberGreater + totalEqualArea) / totalArea
    }

    private fun getDistinctSortedCoordinates(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): List<Double> {
        return (firstNumber.getCoordinates() + secondNumber.getCoordinates()).distinct().sorted()
    }
}