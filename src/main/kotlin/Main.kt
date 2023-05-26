import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import config.ConfigFactory
import gui.dashboard.Dashboard
import gui.dashboard.DashboardViewModel
import gui.dashboard.DashboardViewModelFactory
import input.DefaultInputFactory
import input.Input
import lightOrgan.LightOrgan
import server.Server

val ConfigSingleton = ConfigFactory().create()

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Synesthetic",
        state = rememberWindowState(width = 900.dp, height = 300.dp),
    ) {
        val viewModel = remember { getDashboardViewModel() }
        Dashboard(viewModel)
    }
}

private fun getDashboardViewModel(): DashboardViewModel {
    return DashboardViewModelFactory().create(
        lightOrganStateMachine = getLightOrganStateMachine()
    )
}

private fun getLightOrganStateMachine(): LightOrganStateMachine {
    return LightOrganStateMachine(
        input = getDefaultInput(),
        lightOrgan = getLightOrgan()
    )
}

private fun getDefaultInput(): Input {
    return DefaultInputFactory().create()
}

private fun getLightOrgan(): LightOrgan {
    return LightOrgan(
        subscribers = mutableSetOf(Server())
    )
}