package com.example.fuzzynumbercomparator.comparator


import ComparatorUiState
import androidx.lifecycle.ViewModel
import com.example.fuzzynumbercomparator.domain.ValidateFuzzyNumberUseCase
import com.example.fuzzynumbercomparator.domain.ValidationResult
import com.example.fuzzynumbercomparator.fuzzynumber.DefaultFuzzyNumberFactory
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyMaxOrderComparingStrategy
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumberType
import com.example.fuzzynumbercomparator.fuzzynumber.SimpleComparingStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class FuzzyNumberTextFieldValue(
    val type: FuzzyNumberType = FuzzyNumberType.TRIANGLE,
    val values: List<String> = defaultValuesForType(type),
    val placeholders: List<String> = placeholdersForType(type),
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
        FuzzyNumberType.TRIANGLE -> listOf("Start", "Peak", "End")
        FuzzyNumberType.TRAPEZOID -> listOf("Start", "Start peak", "End peak", "End")
    }
}
internal fun placeholdersForType(type: FuzzyNumberType): List<String> {
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

}

class ComparatorViewModel(): ViewModel() {
    private val _state = MutableStateFlow(ComparatorUiState())
    val state: StateFlow<ComparatorUiState> = _state

    private val validateFuzzyNumber = ValidateFuzzyNumberUseCase()


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
                            placeholders = placeholdersForType(newType),
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
                            placeholders = placeholdersForType(newType),
                            labels = labelsForType(newType)
                            )
                        )
                    }
                }
                validateFuzzyNumbers()
            }
        }
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


            val comparator =
                com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumberOperations(SimpleComparingStrategy())
            val firstNumberGreaterSimple = comparator.compare(firstFuzzyNumber, secondFuzzyNumber)
            val secondNumberGreaterSimple = comparator.compare(secondFuzzyNumber, firstFuzzyNumber)
            comparator.setStrategy(FuzzyMaxOrderComparingStrategy())
            val firstNumberGreaterFMO = comparator.compare(firstFuzzyNumber, secondFuzzyNumber)
            val secondNumberGreaterFMO = comparator.compare(secondFuzzyNumber, firstFuzzyNumber)

            val roundTo = 5
            _state.update { it.copy(
                firstFuzzyNumber = firstFuzzyNumber,
                secondFuzzyNumber = secondFuzzyNumber,
                isResultVisible = true,
                firstNumGreaterResultSimple = String.format("%.${roundTo}f", firstNumberGreaterSimple),
                secondNumGreaterResultSimple = String.format("%.${roundTo}f", secondNumberGreaterSimple),
                firstNumGreaterResultFMO = String.format("%.${roundTo}f", firstNumberGreaterFMO),
                secondNumGreaterResultFMO = String.format("%.${roundTo}f", secondNumberGreaterFMO)
            ) }
        } else {
            _state.update { it.copy(
                isResultVisible = false,
                firstFuzzyNumber = null,
                secondFuzzyNumber = null,
            ) }
        }
    }
}

