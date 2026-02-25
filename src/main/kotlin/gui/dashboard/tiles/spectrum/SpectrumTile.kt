package gui.dashboard.tiles.spectrum

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import config.ConfigSingleton
import gui.basicComponents.*
import dsp.fft.FrequencyBin

@Preview
@Composable
fun SpectrumTile(
    viewModel: SpectrumTileViewModel,
    modifier: Modifier = Modifier
) {
    Tile(modifier) {
        Title()
        SimpleSpacer(dpSize = 12)
        HighlightedFrequency(bin = viewModel.highlightedBin)
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
private fun HighlightedFrequency(bin: FrequencyBin?) {
    DetailText(
        label = "Frequency",
        value = bin?.frequency?.let { "%.2f Hz".format(it) }
    )
}

@Composable
private fun GridSpectrum(viewModel: SpectrumTileViewModel) {
    Grid {
        Spectrum(viewModel)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Spectrum(viewModel: SpectrumTileViewModel) {
    val bins by viewModel.frequencyBins.collectAsState()
    val hoveredIndex = viewModel.highlightedIndex
    val barColor = MaterialTheme.colors.secondary

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .onBinHover(
                binCount = bins.size,
                onHover = { viewModel.highlightedIndex = it },
                onExit = { viewModel.highlightedIndex = null }
            )
    ) {
        val barWidth = size.width / bins.size
        val renderWidth = barWidth + 1f

        bins.forEachIndexed { index, bin ->
            drawBar(index, bin, barWidth, renderWidth, barColor)

            if (index == hoveredIndex) {
                drawHoverHighlight(index, barWidth, renderWidth)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun Modifier.onBinHover(
    binCount: Int,
    onHover: (Int) -> Unit,
    onExit: () -> Unit
): Modifier {
    return this
        .onPointerEvent(PointerEventType.Move) {
            val index = (it.changes.first().position.x / size.width * binCount).toInt()
            onHover(index)
        }
        .onPointerEvent(PointerEventType.Exit) { onExit() }
}

private fun DrawScope.drawBar(
    index: Int,
    bin: FrequencyBin,
    barWidth: Float,
    renderWidth: Float,
    color: Color
) {
    val barHeight = bin.magnitude * size.height * ConfigSingleton.spectrumMultiplier

    drawRect(
        color = color,
        topLeft = Offset(index * barWidth, size.height - barHeight),
        size = Size(renderWidth, barHeight)
    )
}

private fun DrawScope.drawHoverHighlight(
    index: Int,
    barWidth: Float,
    renderWidth: Float
) {
    drawRect(
        color = Color.White.copy(alpha = 0.5f),
        topLeft = Offset(index * barWidth, 0f),
        size = Size(renderWidth, size.height)
    )
}