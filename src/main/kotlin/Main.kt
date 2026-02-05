import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import gateway.GatewayFinder
import gui.Theme
import gui.dashboard.Dashboard
import gui.dashboard.DashboardViewModelFactory
import kotlinx.coroutines.runBlocking
import lightOrgan.LightOrganStateMachine


// TODO: Remove run-blocking when gateway finder is moved
fun main(/*args: Array<String>*/) = runBlocking {
    val stateMachine = LightOrganStateMachine()

    // TODO: Optimize
    val gateway = GatewayFinder().find()
//    val gateway = gatewayPort?.let { Gateway(it) }

    if (gateway != null) {
        stateMachine.addSubscriber(gateway)
    }
    // TODO: Optimize

    launchGUI(stateMachine)
//    if (args.contains("--headless")) {
//        stateMachine.start()
//    } else {
//        launchGUI(stateMachine)
//    }
}

private fun launchGUI(stateMachine: LightOrganStateMachine) = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Synesthetic",
        state = rememberWindowState(width = 900.dp, height = 300.dp),
    ) {
        Theme {
            val viewModel = remember { DashboardViewModelFactory().create(stateMachine) }
            Dashboard(viewModel)
        }
    }
}