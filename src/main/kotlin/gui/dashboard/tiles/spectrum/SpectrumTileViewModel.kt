package gui.dashboard.tiles.spectrum

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import sound.bins.frequency.FrequencyBins
import java.util.*

class SpectrumTileViewModel(
    private val spectrumCreator: SpectrumCreator = SpectrumCreator()
) {

    private var frequencyBins: FrequencyBins = listOf()
    private var hoveredBin: SpectrumBin? = null

    var spectrum by mutableStateOf<Spectrum>(listOf())
        private set
    var hoveredFrequency by mutableStateOf("")
        private set

    fun setFrequencyBins(frequencyBins: FrequencyBins) {
        this.frequencyBins = frequencyBins
        updateSpectrum()
    }

    private fun updateSpectrum() {
        spectrum = spectrumCreator.create(
            frequencyBins = frequencyBins,
            hoveredFrequency = hoveredBin?.frequency
        )
    }

    fun setHoveredBin(hoveredBin: SpectrumBin?) {
        this.hoveredBin = hoveredBin
        updateHoveredFrequency()
        updateSpectrum()
    }

    private fun updateHoveredFrequency() {
        val frequency = hoveredBin?.frequency

        if (frequency != null) {
            val formattedFrequency = frequency.formatToTwoDecimalPoints()
            hoveredFrequency = "$formattedFrequency Hz"
        } else {
            hoveredFrequency = ""
        }
    }

    private fun Float.formatToTwoDecimalPoints(): String {
        return String.format(Locale.US, "%.2f", this)
    }

}
