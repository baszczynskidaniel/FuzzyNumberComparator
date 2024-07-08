package com.example.fuzzynumbercomparator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fuzzynumbercomparator.ui.theme.FuzzyNumberComparatorTheme

@Composable
fun PreviewSurface(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    FuzzyNumberComparatorTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            content()
        }
    }
}