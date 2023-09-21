package sound.frequencyBins.filters

import extensions.between
import lightOrgan.notes.Notes
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import kotlin.math.min

// TODO: Find a more natural filter
class BandPassFilter(
    private val highPassStopFrequency: Float = Notes.C.getFrequency(0),
    private val highPassCornerFrequency: Float = Notes.C.getFrequency(1),
    private val lowPassCornerFrequency: Float = Notes.C.getFrequency(2),
    private val lowPassStopFrequency: Float = Notes.C.getFrequency(3),
) {

    fun filter(frequencyBinList: FrequencyBinList): FrequencyBinList {
        return frequencyBinList
            .applyHighPassFilter()
            .applyLowPassFilter()
    }

    private fun FrequencyBinList.applyHighPassFilter(): FrequencyBinList {
        val filteredBins = mutableListOf<FrequencyBin>()

        for (bin in this) {
            val range = highPassCornerFrequency - highPassStopFrequency
            val rawMultiplier = (bin.frequency - highPassStopFrequency) / range
            val multiplier = rawMultiplier.between(0F, 1F)
            val filteredMagnitude = bin.magnitude * min(multiplier, 1F)
            filteredBins.add(FrequencyBin(bin.frequency, filteredMagnitude))
        }

        return filteredBins
    }

    private fun FrequencyBinList.applyLowPassFilter(): FrequencyBinList {
        val filteredBins = mutableListOf<FrequencyBin>()

        for (bin in this) {
            val range = lowPassCornerFrequency - lowPassStopFrequency
            val rawMultiplier = (bin.frequency - lowPassStopFrequency) / range
            val multiplier = rawMultiplier.between(0F, 1F)
            val filteredMagnitude = bin.magnitude * min(multiplier, 1F)
            filteredBins.add(FrequencyBin(bin.frequency, filteredMagnitude))
        }

        return filteredBins
    }

}