package com.example.fuzzynumbercomparator.fuzzynumber

class FuzzyNumberOperations {
    private var strategy: ComparingStrategy

    fun setStrategy(strategy: ComparingStrategy) {
        this.strategy = strategy
    }
    constructor(strategy: ComparingStrategy) {
        this.strategy = strategy
    }
    constructor() {
        this.strategy = SimpleComparingStrategy()
    }


    fun compare(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): Float {
        return strategy.compare(firstNumber, secondNumber)
    }
}