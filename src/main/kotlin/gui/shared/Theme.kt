package gui.shared

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = darkColors(surface = Color.Black),
        content = content
    )
}