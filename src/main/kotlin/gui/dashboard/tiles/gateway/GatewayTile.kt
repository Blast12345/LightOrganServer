package gui.tiles.gateway

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.*

@Preview
@Composable
fun GatewayTile(
    viewModel: GatewayTileViewModel,
    modifier: Modifier = Modifier
) {
    val isSearching by viewModel.isSearching.collectAsState(initial = false)
    val details by viewModel.gatewayDetails.collectAsState(initial = null)
    val isConnected by viewModel.isConnected.collectAsState(initial = false)

    Tile(modifier) {
        SimpleText(
            text = "Gateway",
            fontSize = 24,
            fontWeight = FontWeight.SemiBold
        )

        SimpleSpacer(12)

        DetailText("Status", if (isConnected) "Connected" else "Not connected")
        DetailText("System Path", details?.systemPath ?: "")
        DetailText("MAC Address", details?.macAddress ?: "")
        DetailText("Firmware", details?.firmwareVersion ?: "")

        SimpleSpacer(12)

        if (details == null || isSearching) {
            SimpleButton(
                title = "Find Gateway",
                isLoading = isSearching,
                action = { viewModel.findGateway() }
            )
        } else if (isConnected) {
            SimpleButton(
                title = "Disconnect",
                isLoading = false,
                action = { viewModel.disconnect() }
            )
        } else {
            SimpleButton(
                title = "Connect",
                isLoading = false,
                action = { viewModel.connect() }
            )
        }
    }
}