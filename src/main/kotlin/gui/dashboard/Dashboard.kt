package gui.dashboard

import SpectrumTile
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gui.dashboard.tiles.clients.ClientsTile
import gui.dashboard.tiles.color.ColorTile
import gui.dashboard.tiles.gateway.GatewayTile
import gui.dashboard.tiles.input.InputTile
import gui.dashboard.tiles.lightOrgan.LightOrganTile

@Preview
@Composable
fun Dashboard(
    viewModel: DashboardViewModel
) {
    Background()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        LightOrganRow(viewModel, Modifier.weight(1f))

        // This will probably become a scrollable grid as more items are added.
        DevicesRow(viewModel, Modifier.height(IntrinsicSize.Max))
    }
}

@Composable
private fun Background() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    )
}

@Composable
private fun LightOrganRow(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {

        LightOrganTile(
            viewModel = viewModel.lightOrganTileViewModel,
            modifier = Modifier.weight(1f).fillMaxSize()
        )

        ColorTile(
            viewModel = viewModel.colorTileViewModel,
            modifier = Modifier.weight(1f).fillMaxSize()
        )

        SpectrumTile(
            viewModel = viewModel.spectrumTileViewModel,
            modifier = Modifier.weight(1f).fillMaxSize()
        )

    }
}

@Composable
private fun DevicesRow(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {

        InputTile(
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        GatewayTile(
            viewModel = viewModel.gatewayTileViewModel,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        ClientsTile(
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

    }
}
