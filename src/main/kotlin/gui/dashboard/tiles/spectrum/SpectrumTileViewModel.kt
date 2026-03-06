package gui.dashboard.tiles.spectrum

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bins.FrequencyBin
import bins.FrequencyBins
import config.ConfigSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import lightOrgan.spectrum.SpectrumManager
import sound.bins.frequency.filters.Crossover

// ENHANCEMENT: Show latency
class SpectrumTileViewModel(
    private val spectrumManager: SpectrumManager,
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val sharingPolicy: SharingStarted = SharingStarted.WhileSubscribed()
) {

    val displayedBins: StateFlow<FrequencyBins> = spectrumManager.frequencyBins
        .map { bins -> bins.filter { it.frequency in lowCrossover.stopFrequency..highCrossover.stopFrequency } }
        .stateIn(scope, sharingPolicy, emptyList())

    var highlightedIndex: Int? by mutableStateOf(null)
    val highlightedBin: FrequencyBin? get() = highlightedIndex?.let { displayedBins.value.getOrNull(it) }

}