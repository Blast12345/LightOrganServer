import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import config.ConfigFactory
import gui.dashboard.Dashboard
import gui.dashboard.DashboardViewModelFactory
import server.Server

val ConfigSingleton = ConfigFactory().create()

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Synesthetic",
        state = rememberWindowState(width = 900.dp, height = 300.dp),
    ) {
        val viewModelFactory = DashboardViewModelFactory()
        val lightOrganStateMachine = LightOrganStateMachine()
        val server = Server()
        lightOrganStateMachine.addSubscriber(server)
        val viewModel = remember { viewModelFactory.create(lightOrganStateMachine) }
        Dashboard(viewModel)
    }
}