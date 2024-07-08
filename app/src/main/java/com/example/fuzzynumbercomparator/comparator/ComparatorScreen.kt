package com.example.fuzzynumbercomparator.comparator

import ComparatorUiState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fuzzynumbercomparator.components.PreviewSurface
import com.example.fuzzynumbercomparator.components.RenderTwoFuzzyNumberColors
import com.example.fuzzynumbercomparator.components.RenderTwoFuzzyNumbers
import com.example.fuzzynumbercomparator.components.SmallTextField
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumber
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumberType
import com.example.fuzzynumbercomparator.fuzzynumber.TrapezoidFuzzyNumber
import com.example.fuzzynumbercomparator.fuzzynumber.TriangleFuzzyNumber

internal val SMALL_PADDING = 8.dp
internal val MEDIUM_PADDING = 16.dp
internal val LARGE_PADDING = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparatorScreen(
    state: ComparatorUiState,
    onEvent: (ComparatorEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING),
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = { Text("Fuzzy number comparator") }
        )
        FNCElevatedCard(
            modifier = Modifier.padding(horizontal = SMALL_PADDING)
        ) {
            NumberMenu(
                modifier = Modifier

                    .fillMaxWidth(),
                label = "First number",
                expanded = state.expandFirstNumberType,
                selectedType = state.firstNumberState.type.toString(),
                onItemClick = {
                    onEvent(ComparatorEvent.OnFirstNumberTypeChange(it))
                },
                onDropDownMenuDismiss = {
                    onEvent(ComparatorEvent.OnDismissFirstNumberType)
                },
                onTypeClick = {
                    onEvent(ComparatorEvent.OnExpandFirstNumberType)
                },
            )
            FuzzyNumberTextFields(
                modifier = Modifier
                    .fillMaxWidth(),

                value = state.firstNumberState,
                onValueChange = {onEvent(ComparatorEvent.FirstNumberChanged(it)) }
            )
        }
        FNCElevatedCard(
            modifier = Modifier.padding(horizontal = SMALL_PADDING)
        ) {

            NumberMenu(
                modifier = Modifier

                    .fillMaxWidth(),
                label = "Second number",
                expanded = state.expandSecondNumberType,
                selectedType = state.secondNumberState.type.toString(),
                onItemClick = {
                    onEvent(ComparatorEvent.OnSecondNumberTypeChange(it))
                },
                onDropDownMenuDismiss = {
                    onEvent(ComparatorEvent.OnDismissSecondNumberType)
                },
                onTypeClick = {
                    onEvent(ComparatorEvent.OnExpandSecondNumberType)
                },
            )
            FuzzyNumberTextFields(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.secondNumberState,
                onValueChange = { onEvent(ComparatorEvent.SecondNumberChanged(it)) }
            )
        }

        AnimatedVisibility(state.isResultVisible) {
            Column {
                if(state.firstFuzzyNumber != null && state.secondFuzzyNumber != null)
                    RenderNumbersCard(
                        modifier = Modifier.padding(horizontal = SMALL_PADDING),
                        firstNumber = state.firstFuzzyNumber,
                        secondNumber = state.secondFuzzyNumber,
                    )
                Spacer(modifier = Modifier.height(SMALL_PADDING))
                ComparisonResultCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMALL_PADDING),
                    firstNumGreaterResult = state.firstNumGreaterResultSimple,
                    secondNumGreaterResult = state.secondNumGreaterResultSimple,
                    comparisonMethod = "Simple comparison",

                )
                Spacer(modifier = Modifier.height(SMALL_PADDING))
                ComparisonResultCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMALL_PADDING),
                    firstNumGreaterResult = state.firstNumGreaterResultFMO,
                    secondNumGreaterResult = state.secondNumGreaterResultFMO,
                    comparisonMethod = "Fuzzy max order",

                    )
            }
        }
    }
}


@Composable
fun FNCElevatedCard(
    modifier: Modifier = Modifier,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(SMALL_PADDING),
            verticalArrangement = Arrangement.spacedBy(SMALL_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Composable
fun RenderNumbersCard(
    modifier: Modifier = Modifier,
    firstNumber: FuzzyNumber,
    secondNumber: FuzzyNumber,
) {
    FNCElevatedCard(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Rendered numbers",
            textAlign = TextAlign.Left,

            )
        RenderTwoFuzzyNumbers(
            firstNumber = firstNumber,
            secondNumber = secondNumber,
            modifier = Modifier
                .height(128.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(SMALL_PADDING),
            colors = RenderTwoFuzzyNumberColors(
                firstFill = MaterialTheme.colorScheme.tertiaryContainer.copy(
                    0.2f
                ),
                secondFill = MaterialTheme.colorScheme.secondaryContainer.copy(
                    0.2f
                ),
                firstStroke = MaterialTheme.colorScheme.tertiary,
                secondStroke = MaterialTheme.colorScheme.secondary,
                intersectionPoints = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun FuzzyNumberDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: () -> Unit,
    text: String,
    onDismiss: (Boolean) -> Unit,
    items: List<String>,
    onItemClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .then(modifier)
    ) {
        ElevatedAssistChip(
            colors = AssistChipDefaults.elevatedAssistChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                trailingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = onClick,
            label = { Text(text = text)},
            trailingIcon = {

                RotatableIcon(
                    rotated = !expanded,
                    imageVector = Icons.Default.KeyboardArrowUp,
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss(!expanded) }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FuzzyNumberTextFields(
    modifier: Modifier = Modifier,
    value: FuzzyNumberTextFieldValue,
    onValueChange: (FuzzyNumberTextFieldValue) -> Unit
) {

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING)
    ) {
        for(index in value.values.indices) {
            Row {
                Spacer(modifier = Modifier.width(SMALL_PADDING))
                //TODO add action done
                SmallTextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = value.values[index],
                    singleLine = true,
                    onValueChange = {
                        val values = value.values.toMutableList()
                        values[index] = it
                        val valueChange = value.copy(
                            values = values
                        )
                        onValueChange(valueChange)
                    },
                    label = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = value.labels[index]
                        )
                    },
                    placeholder = {
                        Text(text = value.placeholders[index])
                    },
                    modifier = Modifier.defaultMinSize(minWidth = 100.dp)
                )
                Spacer(modifier = Modifier.width(SMALL_PADDING))
            }
        }
    }
}

@Composable
private fun NumberMenu(
    label: String,
    modifier: Modifier = Modifier,
    selectedType: String,
    onTypeClick: () -> Unit,
    expanded: Boolean,
    onDropDownMenuDismiss: (Boolean) -> Unit,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground
        )

        FuzzyNumberDropDownMenu(
            expanded = expanded,
            onClick = { onTypeClick() },
            text = selectedType,
            onDismiss = {onDropDownMenuDismiss(it)} ,
            items = FuzzyNumberType.toListOfStrings(),
            onItemClick = {onItemClick(it)}
        )
    }
}

@Preview
@Composable
internal fun ComparatorScreenPreview() {
    val state = ComparatorUiState(
        firstNumberState = FuzzyNumberTextFieldValue(
            FuzzyNumberType.TRIANGLE,
            values = listOf("1", "3", "5")
        ),
        secondNumberState = FuzzyNumberTextFieldValue(
            FuzzyNumberType.TRAPEZOID,
            values = listOf("0", "4", "4.5", "7")
        ),
        firstFuzzyNumber = TriangleFuzzyNumber(1.0, 3.0, 5.0),
        secondFuzzyNumber = TrapezoidFuzzyNumber(0.0, 4.0, 4.5, 7.0),
        isResultVisible = true,
        firstNumGreaterResultSimple = "0.453",
        secondNumGreaterResultSimple = "0.547",
        firstNumGreaterResultFMO = "0.547",
        secondNumGreaterResultFMO = "0.547"

    )
    fun onEvent(event: ComparatorEvent) {

    }
    PreviewSurface(
        darkTheme = true,
        dynamicColor = false,
    ) {
        ComparatorScreen(
            state = state,
            onEvent = { onEvent(it) }

        )
    }
}

@Composable
internal fun ComparisonResultCard(
    modifier: Modifier = Modifier,
    firstNumGreaterResult: String,
    secondNumGreaterResult: String,
    comparisonMethod: String,
) {
    FNCElevatedCard(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = comparisonMethod,
            textAlign = TextAlign.Left,

        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "first > second",
                style = MaterialTheme.typography.bodyMedium
                )
            Text(
                text = firstNumGreaterResult,
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "first < second",
                style = MaterialTheme.typography.bodyMedium
                )
            Text(
                text = secondNumGreaterResult,
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
            )

        }
    }
}

@Preview
@Composable
internal fun ComparisonResultCardPreview() {
    PreviewSurface(modifier = Modifier,
        true, false
    ) {
        ComparisonResultCard(modifier = Modifier
            .fillMaxWidth()
            .padding(SMALL_PADDING),
            firstNumGreaterResult = "0.234",
            secondNumGreaterResult = "0.766",
            comparisonMethod = "Simple comparison",

        )
    }
}