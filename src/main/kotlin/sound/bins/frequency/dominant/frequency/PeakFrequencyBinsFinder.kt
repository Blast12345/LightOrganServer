package sound.bins.frequency.dominant.frequency

import sound.bins.frequency.FrequencyBin
import sound.bins.frequency.FrequencyBins

class PeakFrequencyBinsFinder {

    fun find(frequencyBins: FrequencyBins): FrequencyBins {
        val peaks: MutableList<FrequencyBin> = mutableListOf()

        for (i in 1 until frequencyBins.size - 1) {
            val previousBin = frequencyBins[i - 1]
            val currentBin = frequencyBins[i]
            val nextBin = frequencyBins[i + 1]

            val previousMagnitude = previousBin.magnitude
            val currentBinMagnitude = currentBin.magnitude
            val nextBinMagnitude = nextBin.magnitude

            val isGreaterThanOrEqualToPrevious = currentBinMagnitude >= previousMagnitude
            val isGreaterThanOrEqualToNext = currentBinMagnitude >= nextBinMagnitude
            val isPeak = isGreaterThanOrEqualToPrevious && isGreaterThanOrEqualToNext

            if (isPeak && currentBinMagnitude > 0F) {
//                val interpolatedIndex = i + (previousMagnitude - nextBinMagnitude) / (2 * (previousMagnitude - 2 * currentBinMagnitude + nextBinMagnitude))
//                val interpolatedFrequency = interpolatedIndex * (88200 / 4096) / 128
//                val interpolatedBin = FrequencyBin(interpolatedFrequency, currentBinMagnitude)
                peaks.add(currentBin)
            }
        }

        return peaks
    }

}
