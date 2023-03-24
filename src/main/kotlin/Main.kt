import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import config.ConfigSingleton
import gui.tiles.ColorTile
import gui.tiles.StatsTile
import gui.tiles.SynestheticTile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sound.input.Input
import sound.input.finder.InputFinder

var isActive = true
val activeState = mutableStateOf(isActive)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Synesthetic",
        state = rememberWindowState(width = 900.dp, height = 600.dp),
    ) {
        MaterialTheme {
            gui()
        }
    }
}

//// The clients listening for colors
//val clients: List<Client>,
//// Helps identify a hue for a given frequency
//val colorWheel: ColorWheel,
//// Defines the upper range of bass frequency bins
//val highPassFilter: HighPassFilter,
//// Then number of audio samples to use to identify frequencies
//// Higher values increase accuracy, but also increase latency
//val sampleSize: Int,
//// We interpolate the sample size to a higher value to reveal obscured frequencies,
//// increasing the accuracy of frequency calculations.
//// Higher values and values that are not powers of 2 increase processing time.
//val interpolatedSampleSize: Int,
//// Helps determine how we estimate the dominant frequency's magnitude
//val magnitudeEstimationStrategy: MagnitudeEstimationStrategy,
//// Compensate for low input levels.
//// Setting this "just right" will maximize dynamic range,
//// but setting it too high or too low will decrease dynamic range
//val magnitudeMultiplier: Float,

@Preview
@Composable
private fun gui() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Max)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SynestheticTile(
                isRunning = false,
                startsAutomatically = false,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )

            ColorTile(
                color = Color.Red,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )

            StatsTile(
                durationOfAudioUsed = 0.05f,
                lowestDiscernibleFrequency = 20f,
                resolution = 11f,
                latency = 3.4f,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
    }
}

// MARK: OLD

@Preview
@Composable
private fun audioCheckFrequencyField() {
    val initialString = ConfigSingleton.millisecondsToWaitBetweenCheckingForNewAudio.toString()
    val textState = remember { mutableStateOf(initialString) }

    TextField(
        value = textState.value,
        onValueChange = { newString ->
            val number = newString.filter { it.isDigit() }
            textState.value = number
        },
        label = { Text("Audio Buffer Check Rate") },
        singleLine = true
    )
}


fun launchLightOrgan(): Unit = runBlocking {
    launch {
        createLightOrgan()
        keepAlive()
    }
}

private fun createLightOrgan(): LightOrgan {
    val dataLine = InputFinder().getInput()
    val input = Input(dataLine)
    return LightOrgan(input)
}

private suspend fun keepAlive() {
    while (true) {
        delay(100)
    }
}