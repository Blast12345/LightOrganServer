import androidx.compose.material.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import gui.Theme
import gui.dashboard.Dashboard
import gui.dashboard.DashboardViewModel
import gui.snackbar.SimpleSnackbar
import lightOrgan.LightOrgan
import lightOrgan.color.ColorManager
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

    val lightOrgan = LightOrgan(inputManager, spectrumManager, colorManager)
    lightOrgan.start()

    if (args.contains("--headless")) {
        launchHeadless(lightOrgan)
    } else {
        launchGUI(inputManager, spectrumManager, colorManager)
    }
}

private fun launchGUI(
    inputManager: AudioInputManager,
    spectrumManager: SpectrumManager,
    colorManager: ColorManager,
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
            val snackbar = remember { SimpleSnackbar() }

            Scaffold(
                snackbarHost = { snackbar.Host() }
            ) {
                val viewModel = remember {
                    DashboardViewModel(
                        inputManager,
                        spectrumManager,
                        colorManager,
                        snackbar.controller
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