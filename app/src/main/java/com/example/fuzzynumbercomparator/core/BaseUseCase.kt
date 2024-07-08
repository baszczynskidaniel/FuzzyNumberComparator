package com.example.fuzzynumbercomparator.core

interface BaseUseCase<In, Out> {
    fun execute(input: In): Out
}