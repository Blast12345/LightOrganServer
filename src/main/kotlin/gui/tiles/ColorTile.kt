package gui.tiles

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.ViewModel
import gui.shared.Tile
import gui.shared.wrappers.SimpleSpacer
import gui.shared.wrappers.SimpleText

@Preview
@Composable
fun ColorTile(
    viewModel: ViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(12)
        colorBox(viewModel)
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
private fun colorBox(viewModel: ViewModel) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(viewModel.currentColor.value)
    )
}
