package com.example.fuzzynumbercomparator.fuzzynumber

interface ComparingStrategy {
    fun compare(firstNumber: FuzzyNumber, secondNumber: FuzzyNumber): Float
}