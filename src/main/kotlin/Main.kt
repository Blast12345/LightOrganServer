import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import gateway.Gateway
import gui.Theme
import gui.dashboard.Dashboard
import gui.dashboard.DashboardViewModelFactory
import lightOrgan.LightOrganStateMachine

fun main(/*args: Array<String>*/) {
    val stateMachine = LightOrganStateMachine()
    val gateway = Gateway()

    stateMachine.addSubscriber(gateway)


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