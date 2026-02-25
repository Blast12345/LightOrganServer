package dsp.bins.frequency.dominant.frequency

import dsp.bins.frequency.FrequencyBin
import dsp.bins.frequency.FrequencyBins

class PeakFrequencyBinsFinder {

    fun find(frequencyBins: FrequencyBins): FrequencyBins {
        val peaks: MutableList<FrequencyBin> = mutableListOf()

        for (i in frequencyBins.indices) {
            val previousBin = frequencyBins.elementAtOrNull(i - 1)
            val currentBin = frequencyBins.elementAtOrNull(i)
            val nextBin = frequencyBins.elementAtOrNull(i + 1)

            val previousMagnitude = previousBin?.magnitude ?: 0F
            val currentBinMagnitude = currentBin?.magnitude ?: 0F
            val nextBinMagnitude = nextBin?.magnitude ?: 0F

            val isGreaterThanOrEqualToPrevious = currentBinMagnitude >= previousMagnitude
            val isGreaterThanOrEqualToNext = currentBinMagnitude >= nextBinMagnitude
            val isPeak = isGreaterThanOrEqualToPrevious && isGreaterThanOrEqualToNext

            if (isPeak && currentBin != null) {
                peaks.add(currentBin)
            }
        }

        return peaks
    }

}
