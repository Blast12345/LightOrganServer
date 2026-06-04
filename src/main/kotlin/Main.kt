import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import gui.Theme
import gui.dashboard.Dashboard
import gui.dashboard.DashboardViewModel
import gui.dashboard.snackbar.SharedFlowSnackbarController
import lightOrgan.LightOrgan
import lightOrgan.color.ColorManager
import lightOrgan.gateway.RealGatewayManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager

// ENHANCEMENT: Introduce Frequency type
// ENHANCEMENT: Introduce SampleRate type (which exposes nyquistFrequency)
// ENHANCEMENT: Introduce Magnitude and DBFS types (which can be converted back and forth)
// ENHANCEMENT: Bump JVM SDK version to 21
fun main(args: Array<String>) {
    val inputManager = AudioInputManager()
    val spectrumManager = SpectrumManager()
    val colorManager = ColorManager()
    val realGatewayManager = RealGatewayManager()

    val lightOrgan = LightOrgan(inputManager, spectrumManager, colorManager, realGatewayManager)
    lightOrgan.start()

    if (args.contains("--headless")) {
        launchHeadless(lightOrgan)
    } else {
        launchGUI(inputManager, spectrumManager, colorManager, realGatewayManager)
    }
}

private fun launchGUI(
    inputManager: AudioInputManager,
    spectrumManager: SpectrumManager,
    colorManager: ColorManager,
    realGatewayManager: RealGatewayManager,
) = application {
    val minimumWidth = 1200
    val minimumHeight = 300

    Window(
        title = "Synesthetic",
        state = rememberWindowState(
            width = minimumWidth.dp,
            height = minimumHeight.dp,
        ),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = java.awt.Dimension(minimumWidth, minimumHeight)

        Theme {
            val snackbarController = remember { SharedFlowSnackbarController() }
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                snackbarController.messages.collect { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) {
                val viewModel = remember {
                    DashboardViewModel(
                        inputManager,
                        spectrumManager,
                        colorManager,
                        realGatewayManager,
                        snackbarController
                    )
                }

                Dashboard(viewModel)
            }
        }
    }
}

private fun launchHeadless(lightOrgan: LightOrgan) =
    application {
        TODO("Implement headless mode")
    }