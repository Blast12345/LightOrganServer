package gui.snackbar

import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorSnackbar(snackbarData: SnackbarData) {
    Snackbar(
        snackbarData = snackbarData,
        backgroundColor = Color.Red,
        contentColor = Color.White
    )
}