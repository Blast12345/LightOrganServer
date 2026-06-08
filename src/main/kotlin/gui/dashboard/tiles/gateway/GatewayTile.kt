package gui.tiles.gateway

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.*
import lightOrgan.gateway.Gateway
import lightOrgan.gateway.GatewayManager
import lightOrgan.gateway.SerialGatewayDetails

@Composable
fun GatewayTile(
    viewModel: GatewayTileViewModel,
    modifier: Modifier = Modifier
) {
    val connectionState = viewModel.connectionState.collectAsState().value

    Tile(modifier) {
        SimpleText(
            text = "Gateway",
            fontSize = 24,
            fontWeight = FontWeight.SemiBold
        )

        SimpleSpacer(12)

        when (connectionState) {
            is GatewayManager.State.Disconnected -> {
                SimpleButton("Connect", isLoading = false, action = { viewModel.connect() })
                DetailText("Status", "No Gateway")
            }

            is GatewayManager.State.Connecting -> {
                SimpleButton("Connect", isLoading = true, action = {})
                DetailText("Status", "Connecting...")
            }

            is GatewayManager.State.Connected -> {
                SimpleButton("Disconnect", isLoading = false, action = { viewModel.disconnect() })
                ScrollableColumn {
                    DetailText("Status", "Connected")
                    GatewayDetailsSection(connectionState.gateway.details)
                }
            }

            is GatewayManager.State.Disconnecting -> {
                SimpleButton("Disconnect", isLoading = true, action = {})
                DetailText("Status", "Disconnecting...")
            }
        }
    }
}

@Composable
private fun GatewayDetailsSection(details: Gateway.Details) {
    DetailText("MAC Address", details.macAddress)
    DetailText("Firmware", details.firmwareVersion)

    when (details) {
        is SerialGatewayDetails -> {
            DetailText("Connection", "Serial")
            DetailText("Baud Rate", details.baudRate.toString())
            DetailText("Frame Format", details.frameFormat.notation)
        }
    }
}