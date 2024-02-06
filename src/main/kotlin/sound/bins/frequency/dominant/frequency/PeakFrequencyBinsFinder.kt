package sound.bins.frequency.dominant.frequency

import config.ConfigSingleton
import sound.bins.frequency.FrequencyBin
import sound.bins.frequency.FrequencyBins

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

            if (isPeak && currentBin != null && currentBin.frequency > 40F && currentBin.frequency < 60F) {
                val interpolatedIndex = i + 0.5 * (previousMagnitude - nextBinMagnitude) / (previousMagnitude - 2 * currentBinMagnitude + nextBinMagnitude)
                val binSize = nextBin!!.frequency - currentBin.frequency

                val peak = (interpolatedIndex * binSize) + ConfigSingleton.lowCrossover.stopFrequency
                val moddedBin = FrequencyBin(peak.toFloat(), currentBin.magnitude)
                peaks.add(moddedBin)
            }
        }

        return peaks
    }

}
