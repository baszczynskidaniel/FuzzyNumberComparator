package com.example.fuzzynumbercomparator.fuzzynumber

class SimpleComparingStrategy : ComparingStrategy {
    override fun compare(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): Float {
        val xCoordinates = getDistinctSortedXCoordinates(firstNumber, secondNumber)
        var totalAreaFirstNumberGreater = 0.0f
        var totalAreaSecondNumberGreater = 0.0f
        var totalEqualArea = 0.0f
        var totalArea = 0f
        for (i in 0 until xCoordinates.size - 1) {

            val start = xCoordinates[i]
            val end = xCoordinates[i + 1]
            val firstSubArea = firstNumber.subArea(start, end)
            val secondSubArea = secondNumber.subArea(start, end)

            when {
                secondSubArea == 0.0f && secondNumber.isBiggerThanValue(start) -> {
                    totalAreaSecondNumberGreater += firstSubArea * 2
                }
                firstSubArea == 0.0f && !firstNumber.isBiggerThanValue(start) -> {
                    totalAreaSecondNumberGreater += secondSubArea * 2
                }

                secondSubArea == 0.0f && !secondNumber.isBiggerThanValue(start) -> {
                    totalAreaFirstNumberGreater += firstSubArea * 2
                }
                firstSubArea == 0.0f && firstNumber.isBiggerThanValue(start) -> {
                    totalAreaFirstNumberGreater += secondSubArea * 2
                }

                firstSubArea > secondSubArea -> {
                    totalAreaFirstNumberGreater += (firstSubArea - secondSubArea) * 2 + secondSubArea
                    totalAreaSecondNumberGreater += secondSubArea
                }

                firstSubArea < secondSubArea -> {
                    totalAreaSecondNumberGreater += (secondSubArea - firstSubArea) * 2 + firstSubArea
                    totalAreaFirstNumberGreater += firstSubArea
                }
                // 1 - 0.5     2 - 0.5

                firstSubArea == secondSubArea -> {
                    totalAreaSecondNumberGreater += firstSubArea
                    totalAreaFirstNumberGreater += firstSubArea
                }
                else -> totalAreaFirstNumberGreater += 1000000f
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

    private fun getDistinctSortedXCoordinates(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): List<Float> {
        return (firstNumber.getCoordinates() + secondNumber.getCoordinates()).distinct().sorted()
    }
}