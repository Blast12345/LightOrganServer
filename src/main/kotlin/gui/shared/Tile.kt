package gui.shared

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import gui.shared.wrappers.SimpleText

@Preview
@Composable
private fun ColorTilePreview() {
    Tile {
        SimpleText("Hello World!")
    }
}

@Preview
@Composable
fun Tile(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(modifier) {
        Column(Modifier.background(Color.Black).padding(16.dp)) {
            content()
        }
    }
}