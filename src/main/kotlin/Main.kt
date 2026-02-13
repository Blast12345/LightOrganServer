import androidx.compose.material.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import gateway.GatewayManager
import gui.ErrorSnackbarHost
import gui.Theme
import gui.dashboard.Dashboard
import gui.dashboard.DashboardViewModel
import lightOrgan.LightOrganStateMachine

// TODO: Consolidate coroutine scopes
fun main(args: Array<String>) {
    val gatewayManager = GatewayManager()
    val stateMachine = LightOrganStateMachine(gatewayManager = gatewayManager)

    if (args.contains("--headless")) {
        launchHeadless(stateMachine, gatewayManager)
    } else {
        launchGUI(stateMachine, gatewayManager)
    }
}

private fun launchGUI(stateMachine: LightOrganStateMachine, gatewayManager: GatewayManager) = application {
    val minimumWidth = 900
    val minimumHeight = 500

    Window(
        title = "Synesthetic",
        state = rememberWindowState(
            width = minimumWidth.dp,
            height = minimumHeight.dp,
        ),
        onCloseRequest = ::exitApplication
    ) {
        window.minimumSize = java.awt.Dimension(minimumWidth, minimumHeight)

        Theme {

            Scaffold(
                snackbarHost = { ErrorSnackbarHost() } // TODO: It's important that this is before the VMs... but that seems dangerous
            ) {
                val viewModel = remember { DashboardViewModel.create(stateMachine, gatewayManager) }
                Dashboard(viewModel)
            }

        }
    }
}

private fun launchHeadless(stateMachine: LightOrganStateMachine, gatewayManager: GatewayManager) = application {
    // TODO: Automatically re-find gateway if lost
    TODO("Implement headless mode")
}