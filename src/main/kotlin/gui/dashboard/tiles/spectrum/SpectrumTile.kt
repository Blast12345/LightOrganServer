import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import gui.basicComponents.*
import gui.dashboard.tiles.spectrum.SpectrumBin
import gui.dashboard.tiles.spectrum.SpectrumTileViewModel

@Preview
@Composable
fun SpectrumTile(
    viewModel: SpectrumTileViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        Title()
        SimpleSpacer(dpSize = 12)
        HoveredDetails(frequency = viewModel.hoveredFrequency)
        SimpleSpacer(dpSize = 12)
        GridSpectrum(viewModel)
    }
}

@Composable
private fun Title() {
    SimpleText(
        text = "Spectrum",
        fontSize = 24,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun HoveredDetails(frequency: String?) {
    SimpleText(
        text = "Frequency: $frequency",
        fontSize = 16
    )
}

@Composable
private fun GridSpectrum(viewModel: SpectrumTileViewModel) {
    Grid {
        Spectrum(viewModel)
    }
}

@Composable
private fun Spectrum(viewModel: SpectrumTileViewModel) {
    RowWithEqualColumnWidths(
        children = viewModel.spectrum.map { bin ->
            {
                BinColumn(
                    bin = bin,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
private fun BinColumn(
    bin: SpectrumBin,
    viewModel: SpectrumTileViewModel,
    modifier: Modifier
) {
    Box(modifier = modifier.onHoverChanged(viewModel, bin)) {
        Bar(value = bin.magnitude)

        if (bin.hovered) {
            HighlightBox()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun Modifier.onHoverChanged(viewModel: SpectrumTileViewModel, bin: SpectrumBin): Modifier {
    return this.onPointerEvent(PointerEventType.Enter) {
        viewModel.setHoveredBin(bin)
    }.onPointerEvent(PointerEventType.Exit) {
        viewModel.setHoveredBin(null)
    }
}

@Composable
private fun HighlightBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.5f))
    )
}
