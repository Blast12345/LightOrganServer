package gui.basicComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Bar(value: Float) {
    Column {
        val clearWeight = 1F - value

        if (clearWeight > 0F) {
            ClearBox(modifier = Modifier.weight(clearWeight))
        }

        if (value > 0F) {
            ColoredBox(modifier = Modifier.weight(value))
        }
    }
}

@Composable
private fun ClearBox(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    )
}

@Composable
private fun ColoredBox(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
    )
}
