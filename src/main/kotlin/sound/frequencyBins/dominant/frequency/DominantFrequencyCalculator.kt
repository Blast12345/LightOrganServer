package sound.frequencyBins.dominant.frequency

import math.featureScaling.denormalize
import math.featureScaling.normalizeLogarithmically
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.notes.Notes
import kotlin.math.log
import kotlin.math.pow

class DominantFrequencyCalculator(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val weightedMagnitudeCalculator: WeightedMagnitudeCalculator = WeightedMagnitudeCalculator(),
    private val totalMagnitudeCalculator: TotalMagnitudeCalculator = TotalMagnitudeCalculator()
) {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        val peakBins = getPeakFrequencyBins(frequencyBins)
        val normalizedFrequencies = peakBins.map {
            val normalizedFrequency = it.frequency.normalizeLogarithmically(
                minimum = Notes.C.getFrequency(0),
                maximum = Notes.C.getFrequency(1),
                base = 2F
            ) % 1

            FrequencyBin(normalizedFrequency, it.magnitude)
        }

        val weightedMagnitude = weightedMagnitude(normalizedFrequencies)
        val totalMagnitude = totalMagnitude(normalizedFrequencies)
        val averageNormalizedFrequency = averageFrequency(weightedMagnitude, totalMagnitude) ?: return null

        val freq = averageNormalizedFrequency.denormalizeLogarithmically(
            minimum = Notes.C.getFrequency(0),
            maximum = Notes.C.getFrequency(1),
            base = 2F
        )

        println("Dominant Frequency $freq")

        return freq
    }

    fun Float.denormalizeLogarithmically(minimum: Float, maximum: Float, base: Float): Float {
        val power = this.denormalize(
            minimum = log(minimum, base),
            maximum = log(maximum, base)
        )

        return base.pow(power)
    }

    private fun getPeakFrequencyBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return peakFrequencyBinsFinder.find(frequencyBins)
    }

    private fun weightedMagnitude(frequencyBins: FrequencyBinList): Float {
        return weightedMagnitudeCalculator.calculate(frequencyBins)
    }

    private fun totalMagnitude(frequencyBins: FrequencyBinList): Float {
        return totalMagnitudeCalculator.calculate(frequencyBins)
    }

    private fun averageFrequency(weightedMagnitude: Float, totalMagnitude: Float): Float? {
        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
    }

}