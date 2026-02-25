package gui.dashboard.tiles.spectrum

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.StateFlow
import lightOrgan.spectrum.SpectrumManager
import dsp.bins.frequency.FrequencyBin
import dsp.bins.frequency.FrequencyBins


class SpectrumTileViewModel(
    private val spectrumManager: SpectrumManager
) {

    val frequencyBins: StateFlow<FrequencyBins> = spectrumManager.frequencyBins
    var highlightedIndex: Int? by mutableStateOf(null)
    val highlightedBin: FrequencyBin? get() = highlightedIndex?.let { frequencyBins.value.getOrNull(it) }

    // TODO: Show latency?
    // TODO: Show multiplier?

}
