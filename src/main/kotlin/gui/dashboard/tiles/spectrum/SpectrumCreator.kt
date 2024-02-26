package gui.dashboard.tiles.spectrum

import sound.bins.frequency.FrequencyBins

class SpectrumCreator {

    fun create(frequencyBins: FrequencyBins, hoveredFrequency: Float?): List<SpectrumBin> {
        return frequencyBins.map {
            SpectrumBin(
                frequency = it.frequency,
                magnitude = it.magnitude.coerceIn(0F, 1F),
                hovered = it.frequency == hoveredFrequency
            )
        }
    }

}
