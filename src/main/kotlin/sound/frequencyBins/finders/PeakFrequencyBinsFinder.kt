package sound.frequencyBins.finders

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

class PeakFrequencyBinsFinder {

    fun find(frequencyBins: FrequencyBinList): FrequencyBinList {
        val peaks: MutableList<FrequencyBin> = mutableListOf()

        for (i in frequencyBins.indices) {
            val previousBin = frequencyBins.elementAtOrNull(i - 1)
            val currentBin = frequencyBins.elementAtOrNull(i)
            val nextBin = frequencyBins.elementAtOrNull(i + 1)

            val previousMagnitude = previousBin?.magnitude
            val currentBinMagnitude = currentBin?.magnitude
            val nextBinMagnitude = nextBin?.magnitude

            if (previousMagnitude != null && currentBinMagnitude != null && nextBinMagnitude != null) {
                val isGreaterThanPrevious = currentBinMagnitude > previousMagnitude
                val isGreaterThanNext = currentBinMagnitude > nextBinMagnitude
                val isPeak = isGreaterThanPrevious && isGreaterThanNext

                if (isPeak) {
                    peaks.add(currentBin)
                }
            }
        }

        return peaks
    }

}
