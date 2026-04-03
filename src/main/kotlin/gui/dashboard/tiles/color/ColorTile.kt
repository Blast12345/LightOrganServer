package gui.dashboard.tiles.color

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.SimpleSpacer
import gui.basicComponents.SimpleText
import gui.basicComponents.Tile

// ENHANCEMENT: Fullscreen/second window option
@Preview
@Composable
fun ColorTile(
    viewModel: ColorTileViewModel,
    modifier: Modifier = Modifier
) {
    val color = viewModel.color.collectAsState()

    Tile(modifier) {
        Title()
        SimpleSpacer(dpSize = 12)
        ColorBox(color)
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
private fun ColorBox(color: State<Color>) {
    Canvas(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        drawRect(color = color.value)
    }
}