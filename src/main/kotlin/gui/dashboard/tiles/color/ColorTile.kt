package gui.dashboard.tiles.color

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.Tile
import gui.wrappers.SimpleSpacer
import gui.wrappers.SimpleText

@Preview
@Composable
fun ColorTile(
    viewModel: ColorViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(12)
        colorBox(viewModel.color.value)
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
