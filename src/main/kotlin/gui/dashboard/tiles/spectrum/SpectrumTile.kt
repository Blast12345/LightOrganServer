import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import gui.basicComponents.Grid
import gui.basicComponents.SimpleSpacer
import gui.basicComponents.SimpleText
import gui.basicComponents.Tile
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
    val bins = viewModel.spectrum

    // NOTE: The spectrum will distort if we are not explicit about widths because not all columns will scale at the same time.
    // We set most of the widths explicitly, but leave the final column to fill the remaining space.
    BoxWithConstraints {
        val columnWidth = maxWidth / bins.size

        Row {
            bins.forEach { bin ->
                BinColumn(
                    bin = bin,
                    viewModel = viewModel,
                    modifier = getBinColumnModifier(bin, bins, columnWidth)
                )
            }
        }
    }
}

private fun getBinColumnModifier(bin: SpectrumBin, bins: List<SpectrumBin>, columnWidth: Dp): Modifier {
    return if (bin != bins.last()) {
        Modifier.width(columnWidth)
    } else {
        Modifier.fillMaxWidth()
    }
}

@Composable
private fun BinColumn(
    bin: SpectrumBin,
    viewModel: SpectrumTileViewModel,
    modifier: Modifier
) {
    Box(modifier = modifier.onHoverChanged(viewModel, bin)) {
        Column {
            val clearWeight = 1F - bin.magnitude
            val coloredWeight = bin.magnitude

            if (clearWeight > 0F) {
                ClearBox(modifier = Modifier.weight(clearWeight))
            }

            if (coloredWeight > 0F) {
                ColoredBox(modifier = Modifier.weight(coloredWeight))
            }
        }

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
private fun ClearBox(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    )
}

@Composable
private fun ColoredBox(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
    )
}

@Composable
private fun HighlightBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.5f))
    )
}
