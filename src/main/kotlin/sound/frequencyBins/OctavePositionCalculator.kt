package sound.frequencyBins

import math.featureScaling.denormalize
import math.featureScaling.normalizeLogarithmically
import sound.frequencyBins.dominant.frequency.PeakFrequencyBinsFinder
import sound.frequencyBins.dominant.frequency.TotalMagnitudeCalculator
import sound.frequencyBins.dominant.frequency.WeightedMagnitudeCalculator
import sound.notes.Notes
import kotlin.math.log
import kotlin.math.pow

class OctavePositionCalculator(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val weightedMagnitudeCalculator: WeightedMagnitudeCalculator = WeightedMagnitudeCalculator(),
    private val totalMagnitudeCalculator: TotalMagnitudeCalculator = TotalMagnitudeCalculator()
) {

    private val rootNote = Notes.C

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
        println("Peaks: ${peakBins.map { it.frequency }}")

        return freq.normalizeToOctave()
    }

    // Reference: https://en.wikipedia.org/wiki/Octave
    private fun Float.normalizeToOctave(): Float {
        return this.normalizeLogarithmically(
            minimum = rootNote.getFrequency(0),
            maximum = rootNote.getFrequency(1),
            base = 2F
        )
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