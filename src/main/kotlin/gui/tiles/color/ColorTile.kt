package gui.tiles.color

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gui.basicComponents.SimpleSpacer
import gui.basicComponents.SimpleText
import gui.basicComponents.Tile

@Preview
@Composable
fun ColorTile(
    viewModel: ColorTileViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(12.dp)
        colorBox(viewModel.color)
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
