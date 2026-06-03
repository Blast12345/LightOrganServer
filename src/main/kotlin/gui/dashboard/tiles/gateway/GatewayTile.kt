package gui.tiles.gateway

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.*
import lightOrgan.gateway.GatewayDetails
import lightOrgan.gateway.GatewayManagerState
import lightOrgan.gateway.SerialGatewayDetails

@Composable
fun GatewayTile(
    viewModel: GatewayTileViewModel,
    modifier: Modifier = Modifier
) {
    val connectionState by viewModel.connectionState.collectAsState()

    Tile(modifier) {
        SimpleText(
            text = "Gateway",
            fontSize = 24,
            fontWeight = FontWeight.SemiBold
        )

        SimpleSpacer(12)

        when (val currentState = connectionState) {
            is GatewayManagerState.NoGateway -> {
                SimpleButton("Connect", isLoading = false, action = { viewModel.connect() })
                DetailText("Status", "No Gateway")
            }

            is GatewayManagerState.Connecting -> {
                SimpleButton("Connect", isLoading = true, action = {})
                DetailText("Status", "Connecting...")
            }

            is GatewayManagerState.Connected -> {
                SimpleButton("Disconnect", isLoading = false, action = { viewModel.disconnect() })
                ScrollableColumn {
                    DetailText("Status", "Connected")
                    GatewayDetailsSection(currentState.gateway.details)
                }
            }
        }
    }
}

@Composable
private fun GatewayDetailsSection(details: GatewayDetails) {
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