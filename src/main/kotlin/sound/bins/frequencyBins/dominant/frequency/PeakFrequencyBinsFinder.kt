package sound.bins.frequencyBins.dominant.frequency

import sound.bins.frequencyBins.FrequencyBin
import sound.bins.frequencyBins.FrequencyBinList

class PeakFrequencyBinsFinder {

    fun find(frequencyBins: FrequencyBinList): FrequencyBinList {
        val peaks: MutableList<FrequencyBin> = mutableListOf()

        for (i in frequencyBins.indices) {
            val previousBin = frequencyBins.elementAtOrNull(i - 1)
            val currentBin = frequencyBins.elementAtOrNull(i)
            val nextBin = frequencyBins.elementAtOrNull(i + 1)

            val previousMagnitude = previousBin?.magnitude ?: 0F
            val currentBinMagnitude = currentBin?.magnitude ?: 0F
            val nextBinMagnitude = nextBin?.magnitude ?: 0F

            val isGreaterThanPrevious = currentBinMagnitude > previousMagnitude
            val isGreaterThanNext = currentBinMagnitude > nextBinMagnitude
            val isPeak = isGreaterThanPrevious && isGreaterThanNext

            if (isPeak && currentBin != null) {
                peaks.add(currentBin)
            }
        }

        return peaks
    }

}
