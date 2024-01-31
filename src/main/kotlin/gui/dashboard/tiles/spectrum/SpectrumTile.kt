import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.Grid
import gui.basicComponents.SimpleSpacer
import gui.basicComponents.SimpleText
import gui.basicComponents.Tile
import sound.bins.frequency.FrequencyBins

@Preview
@Composable
fun SpectrumTile(
    viewModel: SpectrumTileViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        title()
        SimpleSpacer(dpSize = 12)
        Grid {
            chart(viewModel.frequencyBins.value)
        }
    }
}

@Composable
private fun title() {
    SimpleText(
        text = "Spectrum",
        fontSize = 24,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun chart(frequencyBins: FrequencyBins) {
    Row {
        frequencyBins.forEach { frequencyBin ->
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                val magnitude = frequencyBin.magnitude.coerceIn(0.01f, 0.99f)
                Spacer(modifier = Modifier.weight(1f - magnitude))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(magnitude)
                        .background(MaterialTheme.colors.secondary)
                )
            }
//            Spacer(modifier = Modifier.width(1.dp))
        }
    }
}
