package color

import math.featureScaling.normalizeLogarithmically
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.dominant.frequency.PeakFrequencyBinsFinder
import sound.notes.Notes

class HueCalculator(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder()
) {

    private val rootNote = Notes.C

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        val peakBins = getPeakFrequencyBins(frequencyBins)

        val peakOctaveBins = peakBins.map {
            OctaveBin(
                position = it.frequency.normalizeToOctave() % 1,
                magnitude = it.magnitude
            )
        }


        val weightedMagnitude = weightedMagnitude(peakOctaveBins)
        val totalMagnitude = totalMagnitude(peakOctaveBins)
        val averageOctavePosition = averageOctavePosition(weightedMagnitude, totalMagnitude) ?: return null

        return averageOctavePosition
    }

    // Reference: https://en.wikipedia.org/wiki/Octave
    private fun Float.normalizeToOctave(): Float {
        return this.normalizeLogarithmically(
            minimum = rootNote.getFrequency(0),
            maximum = rootNote.getFrequency(1),
            base = 2F
        )
    }

    private fun getPeakFrequencyBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return peakFrequencyBinsFinder.find(frequencyBins)
    }

    private fun weightedMagnitude(octaveBins: OctaveBinList): Float {
        var weightedMagnitude = 0F

        for (octaveBin in octaveBins) {
            weightedMagnitude += octaveBin.position * octaveBin.magnitude
        }

        return weightedMagnitude
    }

    private fun totalMagnitude(octaveBins: OctaveBinList): Float {
        return octaveBins.map { it.magnitude }.sum()
    }

    private fun averageOctavePosition(weightedMagnitude: Float, totalMagnitude: Float): Float? {
        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
    }

}

data class OctaveBin(
    val position: Float,
    val magnitude: Float
)

typealias OctaveBinList = List<OctaveBin>