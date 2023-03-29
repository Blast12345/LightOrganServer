package gui.tiles.Stats

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.shared.Tile
import gui.shared.wrappers.SimpleSpacer
import gui.shared.wrappers.SimpleText
import gui.shared.wrappers.SimpleTooltipArea

@Preview
@Composable
fun StatsTile(
    viewModel: StatsViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(12)
        durationOfAudioUsedInformation(viewModel.durationOfAudioUsed)
        SimpleSpacer(6)
        lowestDiscernibleFrequencyInformation(viewModel.lowestDiscernibleFrequency)
        SimpleSpacer(6)
        resolutionInformation(viewModel.frequencyResolution)
        SimpleSpacer(6)
        serverLatencyInformation(viewModel.serverLatency)
    }
}

@Composable
private fun title() {
    SimpleText(
        text = "Stats",
        fontSize = 24,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun durationOfAudioUsedInformation(value: String) {
    SimpleTooltipArea(
        text = "This is the duration of audio used to perform FFT calculation on.\n" +
                "\n" +
                "A lower value will increase perceived responsiveness.\n" +
                "\n" +
                "This is calculated by dividing the number of samples used by the sample rate.\n" +
                "E.g. 4800 samples / 48,000 kHz = 0.1 seconds",
        content = {
            SimpleText(
                // TODO: Maybe create UOM object: Value(24, uom: "Hz")
                text = "Duration audio used: $value",
                fontSize = 12
            )
        }
    )
}

@Composable
private fun lowestDiscernibleFrequencyInformation(value: String) {
    SimpleTooltipArea(
        text = "This is the lowest frequency that can reasonably be identified in the audio.\n" +
                "\n" +
                "A lower value is better because frequencies below the value are not accurate. (Music does not typically go below 30 Hz, so there are diminishing returns beyond this point.)\n" +
                "\n" +
                "This is calculated by dividing 1 by the duration of audio.\n" +
                "E.g. 1 / 0.05 s = 20 Hz",
        content = {
            SimpleText(
                text = "Lowest discernible frequency: $value",
                fontSize = 12
            )
        }
    )
}

@Composable
private fun resolutionInformation(value: String) {
    SimpleTooltipArea(
        text = "This is the ability to discern between frequencies.\n" +
                "\n" +
                "A lower value is better because we can more easily differentiate between two frequencies (e.g. 45 Hz vs 46 Hz).\n" +
                "\n" +
                "This is calculated by dividing the sample rate by the number of samples used." +
                "E.g. 48,000 Hz / 4800 samples = 10 Hz",
        content = {
            SimpleText(
                text = "Frequency Resolution: $value",
                fontSize = 12
            )
        }
    )
}

@Composable
private fun serverLatencyInformation(value: String) {
    SimpleTooltipArea(
        text = "This is time between colors being broadcast to the clients.\n" +
                "\n" +
                "A lower value is better because the color will be shown closer to when the relevant sound(s) occurred.\n" +
                "\n" +
                "Many factors impact this, so it cannot simply be calculated.",
        content = {
            SimpleText(
                text = "Server Latency: $value",
                fontSize = 12
            )
        }
    )
}
