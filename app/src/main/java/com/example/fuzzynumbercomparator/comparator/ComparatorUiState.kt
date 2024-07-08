import com.example.fuzzynumbercomparator.comparator.FuzzyNumberTextFieldValue
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumber
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumberType

data class ComparatorUiState(
    val firstNumberState: FuzzyNumberTextFieldValue = FuzzyNumberTextFieldValue(),
    val secondNumberState: FuzzyNumberTextFieldValue = FuzzyNumberTextFieldValue(),
    val firstNumGreaterResultSimple: String = "",
    val secondNumGreaterResultSimple: String = "",
    val firstNumGreaterResultFMO: String = "",
    val secondNumGreaterResultFMO: String = "",
    val isResultVisible: Boolean = false,
    val firstFuzzyNumber: FuzzyNumber? = null,
    val secondFuzzyNumber: FuzzyNumber? = null,

    val expandFirstNumberType: Boolean = false,
    val expandSecondNumberType: Boolean = false,
    val firstNumberType: FuzzyNumberType = FuzzyNumberType.TRIANGLE,
    val secondNumberType: FuzzyNumberType = FuzzyNumberType.TRIANGLE,

    )

