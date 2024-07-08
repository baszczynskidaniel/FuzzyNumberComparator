package com.example.fuzzynumbercomparator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fuzzynumbercomparator.ui.theme.FuzzyNumberComparatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    Box() {
        BasicTextField(
            maxLines = maxLines,
            minLines = minLines,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            readOnly = readOnly,
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = Modifier
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinHeight,
                    minHeight = TextFieldDefaults.MinHeight
                )
                .width(IntrinsicSize.Min)
                .then(modifier),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            textStyle = textStyle,
        ) {
            TextFieldDefaults.DecorationBox(
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                prefix = prefix,
                suffix = suffix,
                supportingText = supportingText,
                value = value,
                innerTextField = it,
                enabled = enabled,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(),
                contentPadding = if(label != null) TextFieldDefaults.contentPaddingWithLabel() else TextFieldDefaults.contentPaddingWithoutLabel(),
                container = {
                    TextFieldDefaults.ContainerBox(
                        enabled,
                        isError,
                        interactionSource,
                        colors = colors,
                        shape = shape
                    )
                },
            )
        }
    }
}



@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun TextFieldListPreview() {

    data class uiState(
        val values: List<String> = listOf("", "", ""),
        val labels: List<String> = listOf("A", "B", "C")
    )

    var state by remember {
        mutableStateOf(uiState())
    }

    FuzzyNumberComparatorTheme(
        false,
        false
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for(index in state.labels.indices) {
                        SmallTextField(
                            label = {
                                Text(text = state.labels[index])
                            },
                            value = state.values[index], onValueChange = {

                                val values = state.values.toMutableList()
                                values[index] = it
                                state = state.copy(
                                    values = values
                                )
                            },
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                            colors = TextFieldDefaults.colors(


                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.defaultMinSize(100.dp)

                        )
                    }
                }
            }
        }
    }


}



@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun SmallTextFieldPreview() {
    FuzzyNumberComparatorTheme(
        false,
        false
    ) {
        var value by remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(

                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SmallTextField(
                        label = {
                            Text(text = value)
                        },
                        value = value, onValueChange = { value = it},
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        colors = TextFieldDefaults.colors(


                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.defaultMinSize(100.dp)

                    )
                }
            }
        }
    }
}
