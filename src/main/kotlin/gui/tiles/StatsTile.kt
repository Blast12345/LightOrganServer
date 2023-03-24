package gui.tiles

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
private fun StatsTilePreview() {
    StatsTile(
        durationOfAudioUsed = 0.05f,
        lowestDiscernibleFrequency = 20f,
        resolution = 11f,
        latency = 3.4f
    )
}

@Preview
@Composable
fun StatsTile(
    durationOfAudioUsed: Float,
    lowestDiscernibleFrequency: Float,
    resolution: Float,
    latency: Float,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(12)
        durationOfAudioUsedInformation(durationOfAudioUsed)
        SimpleSpacer(6)
        lowestDiscernibleFrequencyInformation(lowestDiscernibleFrequency)
        SimpleSpacer(6)
        resolutionInformation(resolution)
        SimpleSpacer(6)
        latencyInformation(latency)
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
private fun durationOfAudioUsedInformation(value: Float) {
    SimpleTooltipArea(
        text = "This is the duration of audio used to perform FFT calculation on. A longer duration will increase frequency resolution, but decrease perceived responsiveness. A short duration will decrease frequency resolution, but increase perceived responsiveness.",
        content = {
            SimpleText(
                // TODO: Maybe create UOM object: Value(24, uom: "hz")
                text = "Duration audio used: $value seconds",
                fontSize = 12
            )
        }
    )
}

@Composable
private fun lowestDiscernibleFrequencyInformation(value: Float) {
    SimpleTooltipArea(
        text = "One cycle of a frequency is required to reasonably discern that it occurred in a sound signal.\n\nThe duration of a cycle is 1 divided by the frequency.\nE.g. 1 / 20hz = 0.05s\n\nThe lowest discernible frequency is 1 divided by the signal duration.\nE.g. 1 / 0.05s = 20hz",
        content = {
            SimpleText(
                text = "Lowest discernible frequency: $value hz",
                fontSize = 12
            )
        }
    )
}

@Composable
private fun resolutionInformation(value: Float) {
    SimpleText(
        text = "Resolution: $value hz",
        fontSize = 12
    )
}


@Composable
private fun latencyInformation(value: Float) {
    SimpleText(
        text = "Latency: $value ms",
        fontSize = 12
    )
}
