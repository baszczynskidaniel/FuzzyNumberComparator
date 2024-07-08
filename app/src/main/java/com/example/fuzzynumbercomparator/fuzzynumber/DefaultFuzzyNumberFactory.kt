package com.example.fuzzynumbercomparator.fuzzynumber

class DefaultFuzzyNumberFactory: FuzzyNumberFactory {
    override fun createFromFuzzyNumberTypeWithParams(
        type: FuzzyNumberType,
        params: List<Double>
    ): FuzzyNumber {
        when(type) {
            FuzzyNumberType.TRIANGLE -> {
                val a = params[0]
                val b = params[1]
                val c = params[2]
                return TriangleFuzzyNumber(a, b, c)
            }
            FuzzyNumberType.TRAPEZOID -> {
                val a = params[0]
                val b = params[1]
                val c = params[2]
                val d = params[3]
                return TrapezoidFuzzyNumber(a, b, c, d)
            }
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}