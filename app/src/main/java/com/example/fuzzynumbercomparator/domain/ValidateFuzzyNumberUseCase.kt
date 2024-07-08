package com.example.fuzzynumbercomparator.domain

import com.example.fuzzynumbercomparator.comparator.FuzzyNumberTextFieldValue
import com.example.fuzzynumbercomparator.core.BaseUseCase
import com.example.fuzzynumbercomparator.fuzzynumber.DefaultFuzzyNumberFactory
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumberType
import com.example.fuzzynumbercomparator.fuzzynumber.TrapezoidFuzzyNumber
import com.example.fuzzynumbercomparator.fuzzynumber.TriangleFuzzyNumber

class ValidateFuzzyNumberUseCase: BaseUseCase<FuzzyNumberTextFieldValue, ValidationResult> {
    override fun execute(input: FuzzyNumberTextFieldValue): ValidationResult
        {
            if(input.values.any {it.isBlank()}) {
            return ValidationResult(
                successful = false,
                errorMessage = "The number can't be blank"
            )
        }
        val values: List<Double?> = input.values.map { it.toDoubleOrNull() }

        if(values.any {it == null}) {
            return ValidationResult(
                successful = false,
                errorMessage = "Not all fields contains number"
            )
        }

        try {
            val factory = DefaultFuzzyNumberFactory()
            factory.createFromFuzzyNumberTypeWithParams(input.type, values.map { it!!.toDouble() })
        } catch (e: Exception) {
            return ValidationResult(
                successful = false,
                errorMessage = e.message
            )
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}