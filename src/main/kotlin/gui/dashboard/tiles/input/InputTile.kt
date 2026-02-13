package gui.dashboard.tiles.input

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.SimpleText
import gui.basicComponents.Tile

@Preview
@Composable
fun InputTile(
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
    }
}

@Composable
private fun title() {
    SimpleText(
        text = "Input",
        fontSize = 24,
        fontWeight = FontWeight.SemiBold
    )
}