package gui.dashboard

import SpectrumTile
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gui.dashboard.tiles.color.ColorTile
import gui.dashboard.tiles.lightOrgan.LightOrganTile

@Preview
@Composable
fun Dashboard(
    viewModel: DashboardViewModel
) {
    Background()
    MainRow(viewModel)
}

@Composable
private fun Background() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    )
}

@Composable
private fun MainRow(viewModel: DashboardViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(16.dp)
    ) {

        LightOrganTile(
            viewModel = viewModel.lightOrganTileViewModel
        )

        ColorTile(
            viewModel = viewModel.colorTileViewModel,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        SpectrumTile(
            viewModel = viewModel.spectrumTileViewModel,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

    }
}
