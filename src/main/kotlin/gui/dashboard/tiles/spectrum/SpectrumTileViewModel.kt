package gui.dashboard.tiles.spectrum

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import config.ConfigSingleton
import dsp.bins.FrequencyBin
import dsp.bins.FrequencyBins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import lightOrgan.spectrum.SpectrumManager

// ENHANCEMENT: Show latency
class SpectrumTileViewModel(
    private val spectrumManager: SpectrumManager,
    private val config: SpectrumGuiConfig = ConfigSingleton.spectrumGui,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val sharingPolicy: SharingStarted = SharingStarted.WhileSubscribed()
) {

    // ENHANCEMENT: Expose GUI configuration for these, at which point we should update displayed bins on change.
    var lowestFrequency: Float by mutableStateOf(config.lowestFrequency)
    var highestFrequency: Float by mutableStateOf(config.highestFrequency)
    var scale: Float by mutableStateOf(config.scale)

    val displayedBins: StateFlow<FrequencyBins> = spectrumManager.frequencyBins
        .map { it.filter { bin -> bin.frequency in lowestFrequency..highestFrequency } }
        .map { it.map { bin -> bin.copy(magnitude = bin.magnitude * scale) } }
        .stateIn(scope, sharingPolicy, emptyList())

    var highlightedIndex: Int? by mutableStateOf(null)
    val highlightedBin: FrequencyBin? get() = highlightedIndex?.let { displayedBins.value.getOrNull(it) }

}