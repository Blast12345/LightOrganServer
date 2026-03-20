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
import gui.dashboard.SnackbarController
import lightOrgan.LightOrgan
import lightOrgan.color.ColorManager
import lightOrgan.input.AudioInputManager
import lightOrgan.spectrum.SpectrumManager

// TODO: Consolidate coroutine scopes
// ENHANCEMENT: Add audio output? This can also delay audio to keep things aligned.
fun main(args: Array<String>) {
    val audioInputManager = AudioInputManager()
    val spectrumManager = SpectrumManager()
    val colorManager = ColorManager()

    val lightOrgan = LightOrgan(
        audioInputManager,
        spectrumManager,
        colorManager
    )

    if (args.contains("--headless")) {
        launchHeadless(lightOrgan)
    } else {
        launchGUI(lightOrgan)
    }
}

private fun launchGUI(
    lightOrgan: LightOrgan
) =
    application {
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
                val snackbarController = remember { SnackbarController() }
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    snackbarController.messages.collect { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) {
                    val viewModel = remember { DashboardViewModel(lightOrgan, snackbarController) }
                    Dashboard(viewModel)
                }
            }
        }
    }

private fun launchHeadless(lightOrgan: LightOrgan) =
    application {
        TODO("Implement headless mode")
    }