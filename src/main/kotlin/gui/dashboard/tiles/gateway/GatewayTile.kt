package gui.dashboard.tiles.gateway

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.*

@Preview
@Composable
fun GatewayTile(
    viewModel: GatewayTileViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(dpSize = 12)
        status(viewModel)
        details(viewModel)
        SimpleSpacer(dpSize = 12)

        if (viewModel.isConnected) {
            disconnectButton(viewModel)
        } else {
            connectButton(viewModel)
        }
    }
}

@Composable
private fun title() {
    SimpleText(
        text = "Gateway",
        fontSize = 24,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun status(viewModel: GatewayTileViewModel) {
    val value = if (viewModel.isConnected) "Connected" else "Not connected"

    DetailText("Status", value)
}

@Composable
private fun details(viewModel: GatewayTileViewModel) {
    DetailText("System Path", viewModel.systemPath)
    DetailText("MAC Address", viewModel.macAddress)
    DetailText("Firmware", viewModel.firmwareVersion)
}

@Composable
private fun connectButton(viewModel: GatewayTileViewModel) {
    SimpleButton(
        title = "Connect",
        isLoading = viewModel.isSearching,
        action = { viewModel.connect() },
    )
}

@Composable
private fun disconnectButton(viewModel: GatewayTileViewModel) {
    SimpleButton(
        title = "Disconnect",
        isLoading = false,
        action = { viewModel.disconnect() },
    )
}