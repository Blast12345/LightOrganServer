package gui.tiles

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import gui.shared.Tile
import gui.shared.wrappers.SimpleSpacer
import gui.shared.wrappers.SimpleText

@Preview
@Composable
private fun ColorTilePreview() {
    ColorTile(
        color = Color.Red
    )
}

@Preview
@Composable
fun ColorTile(
    color: Color,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(12)
        colorBox(color)
    }
}

@Composable
private fun title() {
    SimpleText(
        text = "Color",
        fontSize = 24,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun colorBox(color: Color) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color)
    )
}