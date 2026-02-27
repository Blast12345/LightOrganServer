package gui.dashboard.tiles.color

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.SimpleSpacer
import gui.basicComponents.SimpleText
import gui.basicComponents.Tile

@Preview
@Composable
fun ColorTile(
    viewModel: ColorTileViewModel,
    modifier: Modifier = Modifier
) {
    val color by viewModel.color.collectAsState()

    Tile(modifier) {
        Title()
        SimpleSpacer(dpSize = 12)
        ColorBox(color.toComposeColor())
    }
}

@Composable
private fun Title() {
    SimpleText(
        text = "Color",
        fontSize = 24,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ColorBox(color: Color) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color)
    )
}