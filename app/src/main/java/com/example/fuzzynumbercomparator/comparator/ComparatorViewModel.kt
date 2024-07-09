package com.example.fuzzynumbercomparator.comparator


import ComparatorUiState
import androidx.lifecycle.ViewModel
import com.example.fuzzynumbercomparator.domain.ValidateFuzzyNumberUseCase
import com.example.fuzzynumbercomparator.domain.ValidationResult
import com.example.fuzzynumbercomparator.fuzzynumber.DefaultFuzzyNumberFactory
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumberOperations
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumberType
import com.example.fuzzynumbercomparator.fuzzynumber.SimpleComparingStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class FuzzyNumberTextFieldValue(
    val type: FuzzyNumberType = FuzzyNumberType.TRIANGLE,
    val values: List<String> = defaultValuesForType(type),
    val labels: List<String> = labelsForType(type),
)

internal fun defaultValuesForType(type: FuzzyNumberType): List<String> {
    return when(type) {
        FuzzyNumberType.TRIANGLE -> listOf("", "", "")
        FuzzyNumberType.TRAPEZOID -> listOf("", "", "", "")
    }
}

internal fun labelsForType(type: FuzzyNumberType): List<String> {
    return when(type) {
        FuzzyNumberType.TRIANGLE -> listOf("A", "B", "C")
        FuzzyNumberType.TRAPEZOID -> listOf("A", "B", "C", "D")
    }
}


sealed class ComparatorEvent {
    data class FirstNumberChanged(val numberChange: FuzzyNumberTextFieldValue): ComparatorEvent()
    data class SecondNumberChanged(val numberChange: FuzzyNumberTextFieldValue): ComparatorEvent()
    object OnExpandFirstNumberType: ComparatorEvent()
    object OnExpandSecondNumberType: ComparatorEvent()
    object OnDismissFirstNumberType: ComparatorEvent()
    object OnDismissSecondNumberType: ComparatorEvent()
    data class OnFirstNumberTypeChange(val typeChange: String): ComparatorEvent()
    data class OnSecondNumberTypeChange(val typeChange: String): ComparatorEvent()
    data class OnAlphaChange(val alphaChange: Float): ComparatorEvent()
    data class OnBetaChange(val betaChange: Float): ComparatorEvent()

}

class ComparatorViewModel(): ViewModel() {
    private val _state = MutableStateFlow(ComparatorUiState())
    val state: StateFlow<ComparatorUiState> = _state

    private val validateFuzzyNumber = ValidateFuzzyNumberUseCase()
    private val comparator = FuzzyNumberOperations(SimpleComparingStrategy(_state.value.alpha, _state.value.beta))


    fun onEvent(event: ComparatorEvent) {
        when(event) {
            is ComparatorEvent.FirstNumberChanged -> {
                _state.update { it.copy(firstNumberState = event.numberChange) }
                validateFuzzyNumbers()
            }
            is ComparatorEvent.SecondNumberChanged -> {
                _state.update { it.copy(secondNumberState = event.numberChange) }
                validateFuzzyNumbers()
            }
            ComparatorEvent.OnDismissFirstNumberType -> {
                _state.update { it.copy(
                    expandFirstNumberType = false
                ) }
            }

            ComparatorEvent.OnDismissSecondNumberType -> {
                _state.update { it.copy(
                    expandSecondNumberType = false
                ) }
            }
            ComparatorEvent.OnExpandFirstNumberType -> {
                _state.update { it.copy(
                    expandFirstNumberType = !it.expandFirstNumberType
                ) }
            }
            ComparatorEvent.OnExpandSecondNumberType -> {
                _state.update { it.copy(
                    expandSecondNumberType = !it.expandSecondNumberType
                ) }
            }
            is ComparatorEvent.OnFirstNumberTypeChange -> {
                val newType = FuzzyNumberType.fromString(event.typeChange)
                newType?.let {
                    _state.update { it.copy(
                        firstFuzzyNumber = null,
                        isResultVisible = false,
                        expandFirstNumberType = false,
                        firstNumberState = it.firstNumberState.copy(
                            type = newType,
                            values = defaultValuesForType(newType),

                            labels = labelsForType(newType)
                        )
                    )

                    }
                    validateFuzzyNumbers()
                }
            }
            is ComparatorEvent.OnSecondNumberTypeChange -> {
                val newType = FuzzyNumberType.fromString(event.typeChange)
                newType?.let {
                    _state.update { it.copy(
                        secondFuzzyNumber = null,
                        isResultVisible = false,
                        expandSecondNumberType = false,
                        secondNumberState = it.secondNumberState.copy(
                            type = newType,
                            values = defaultValuesForType(newType),

                            labels = labelsForType(newType)
                            )
                        )
                    }
                }
                validateFuzzyNumbers()
            }

            is ComparatorEvent.OnAlphaChange -> {

                _state.update { it.copy(
                    alpha = event.alphaChange
                    )
                }
                comparator.setStrategy(SimpleComparingStrategy(_state.value.alpha, _state.value.beta))
                calculateComparison()

            }
            is ComparatorEvent.OnBetaChange -> {
                _state.update { it.copy(
                    beta = event.betaChange
                )
                }
                comparator.setStrategy(SimpleComparingStrategy(_state.value.alpha, _state.value.beta))
                calculateComparison()
            }
        }
    }

    private fun calculateComparison() {
        if(_state.value.firstFuzzyNumber == null || _state.value.secondFuzzyNumber == null)
            return
        val firstNumberGreaterResult = comparator.compare(_state.value.firstFuzzyNumber!!, _state.value.secondFuzzyNumber!!)
        val secondNumberGreaterResult = comparator.compare(_state.value.secondFuzzyNumber!!, _state.value.firstFuzzyNumber!!)

        val roundTo = 8

        _state.update { it.copy(
            isResultVisible = true,
            firstNumGreaterResultSimple = String.format("%.${roundTo}f", firstNumberGreaterResult),
            secondNumGreaterResultSimple = String.format("%.${roundTo}f", secondNumberGreaterResult),
        ) }
    }

    private fun validateFuzzyNumbers() {
        val number1Result = validateFuzzyNumber.execute(state.value.firstNumberState)
        val number2Result = validateFuzzyNumber.execute(state.value.secondNumberState)

        val hasError = listOf<ValidationResult>(
            number1Result,
            number2Result
        ).any {
            !it.successful

        }

        if(!hasError) {
            val factory = DefaultFuzzyNumberFactory()
            val values1: List<Double> = _state.value.firstNumberState.values.map { it.toDouble() }
            val firstFuzzyNumber = factory.createFromFuzzyNumberTypeWithParams(_state.value.firstNumberState.type, values1)
            val values2: List<Double> = _state.value.secondNumberState.values.map { it.toDouble() }
            val secondFuzzyNumber = factory.createFromFuzzyNumberTypeWithParams(_state.value.secondNumberState.type, values2)




            _state.update { it.copy(
                firstFuzzyNumber = firstFuzzyNumber,
                secondFuzzyNumber = secondFuzzyNumber,
                isResultVisible = true,
                )
            }
            calculateComparison()
        } else {
            _state.update { it.copy(
                isResultVisible = false,
                firstFuzzyNumber = null,
                secondFuzzyNumber = null,
            ) }
        }
    }
}

