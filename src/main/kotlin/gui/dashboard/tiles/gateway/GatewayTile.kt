package gui.tiles.gateway

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.*
import lightOrgan.gateway.GatewayDetails
import lightOrgan.gateway.GatewayManagerState

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

        DetailText("Status", connectionState.displayName)
        // TODO: Additional details like protocol, baud rate, etc
        DetailText("MAC Address", connectionState.gatewayDetails?.macAddress)
        DetailText("Firmware", connectionState.gatewayDetails?.firmwareVersion)

        SimpleSpacer(12)

        when (connectionState) {
            is GatewayManagerState.NoGateway -> SimpleButton(
                title = "Connect",
                isLoading = false,
                action = { viewModel.connect() }
            )

            is GatewayManagerState.Connecting -> SimpleButton(
                title = "Connecting",
                isLoading = true,
                action = {}
            )

            is GatewayManagerState.Connected -> SimpleButton(
                title = "Disconnect",
                isLoading = false,
                action = { viewModel.disconnect() }
            )
        }
    }
}

private val GatewayManagerState.displayName: String
    get() = when (this) {
        is GatewayManagerState.NoGateway -> "No Gateway"
        is GatewayManagerState.Connecting -> "Connecting..."
        is GatewayManagerState.Connected -> "Connected"
    }

private val GatewayManagerState.gatewayDetails: GatewayDetails?
    get() = (this as? GatewayManagerState.Connected)?.gateway?.details