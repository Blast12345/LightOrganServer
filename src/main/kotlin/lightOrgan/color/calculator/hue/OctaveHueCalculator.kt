package lightOrgan.color.calculator.hue

import bins.FrequencyBins
import bins.LowPassFilter
import bins.PeakFrequencyBinsCalculator
import config.ConfigSingleton
import math.geometry.Angle
import music.Tuning
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class OctaveHueCalculator(
    private val lowPassFilter: LowPassFilter? = ConfigSingleton.lowPassFilter,
    private val peakFrequencyBinsCalculator: PeakFrequencyBinsCalculator = PeakFrequencyBinsCalculator(),
    private val tuning: Tuning = Tuning.western(),
) : HueCalculator {

    override fun calculate(frequencyBins: FrequencyBins): Float? {
        val trimmedBins = frequencyBins.trimLowPassFilter()
        val peakFrequencyBins = peakFrequencyBinsCalculator.calculate(trimmedBins)

        if (peakFrequencyBins.isEmpty()) return null

        val averageAngle = weightedAverageAngle(peakFrequencyBins)
        val normalizedAngle = averageAngle.turns.mod(1f).toFloat()

        return normalizedAngle
    }

    private fun FrequencyBins.trimLowPassFilter(): FrequencyBins {
        return lowPassFilter?.trim(this) ?: this
    }

    // weighted average with respect to octave
//    private fun weightedAverageAngle(frequencyBins: FrequencyBins): Angle {
//        var weightedSum = 0.0
//        var totalMagnitude = 0f
//
//        for (frequencyBin in frequencyBins) {
//            val turns = tuning.getHelicalAngle(frequencyBin.frequency).turns
//            weightedSum += frequencyBin.magnitude * turns
//            totalMagnitude += frequencyBin.magnitude
//        }
//
//        return Angle.fromTurns(weightedSum / totalMagnitude)
//    }

    // weighted average without respect to octave
    private fun weightedAverageAngle(frequencyBins: FrequencyBins): Angle {
        var sumCos = 0F
        var sumSin = 0F

        for (frequencyBin in frequencyBins) {
            val angle = tuning.getHelicalAngle(frequencyBin.frequency)
            sumCos += frequencyBin.magnitude * cos(angle.radians.toFloat())
            sumSin += frequencyBin.magnitude * sin(angle.radians.toFloat())
        }

        val resultantMagnitude = sqrt(sumCos * sumCos + sumSin * sumSin)
        val totalMagnitude = frequencyBins.map { it.magnitude }.sum()
        val confidence = if (totalMagnitude > 0) resultantMagnitude / totalMagnitude else 0f
        val peakMagninitude = frequencyBins.maxOfOrNull { it.magnitude } ?: 0f
        if (confidence < 0.05f && peakMagninitude > 0.1f) {
            println("Indeterminate hue: confidence=$confidence, peaks=$frequencyBins")
        }

        val radians = atan2(sumSin, sumCos)
        return Angle.fromRadians(radians)
    }

}