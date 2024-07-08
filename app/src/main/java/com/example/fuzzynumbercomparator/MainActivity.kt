package com.example.fuzzynumbercomparator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fuzzynumbercomparator.comparator.ComparatorScreen
import com.example.fuzzynumbercomparator.comparator.ComparatorViewModel
import com.example.fuzzynumbercomparator.ui.theme.FuzzyNumberComparatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FuzzyNumberComparatorTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    val viewModel by viewModels<ComparatorViewModel>()
                    val state = viewModel.state.collectAsState()
                    ComparatorScreen(state = state.value, onEvent = viewModel::onEvent)
                }
            }
        }
    }
}

