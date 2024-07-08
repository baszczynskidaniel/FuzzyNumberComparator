package com.example.fuzzynumbercomparator.fuzzynumber

interface FuzzyNumberFactory {
    fun createFromFuzzyNumberTypeWithParams(type: FuzzyNumberType, params: List<Float>): FuzzyNumber
}