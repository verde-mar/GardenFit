package it.unipi.gardenfit.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Display a title
 */
@Composable
fun LargeTitle (title: String) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .fillMaxHeight()

        ) {
        Column {
            Text(
                title,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}
