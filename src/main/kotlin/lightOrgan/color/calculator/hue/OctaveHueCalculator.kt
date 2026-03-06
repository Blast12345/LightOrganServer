package lightOrgan.color.calculator.hue

import bins.FrequencyBins
import bins.PeakFrequencyBinsCalculator
import config.ConfigSingleton
import math.geometry.Angle
import music.Tuning
import sound.bins.frequency.filters.Crossover
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class OctaveHueCalculator(
    private val highCrossover: Crossover? = ConfigSingleton.highCrossover,
    private val peakFrequencyBinsCalculator: PeakFrequencyBinsCalculator = PeakFrequencyBinsCalculator(),
    private val tuning: Tuning = Tuning.western(),
) : HueCalculator {

    override fun calculate(frequencyBins: FrequencyBins): Float? {
        val filteredBins = frequencyBins.belowCrossover()
        val peakFrequencyBins = peakFrequencyBinsCalculator.calculate(filteredBins)

        if (peakFrequencyBins.isEmpty()) return null

        val averageAngle = weightedAverageAngle(peakFrequencyBins)
        val normalizedAngle = averageAngle.turns.mod(1f).toFloat()

        return normalizedAngle
    }

    private fun FrequencyBins.belowCrossover(): FrequencyBins {
        val cutoffFrequency = highCrossover?.stopFrequency ?: Float.MAX_VALUE
        return this.filter { it.frequency <= cutoffFrequency }
    }

    private fun weightedAverageAngle(frequencyBins: FrequencyBins): Angle {
        var sumCos = 0F
        var sumSin = 0F

        for (frequencyBin in frequencyBins) {
            val angle = tuning.getPositionInOctave(frequencyBin.frequency)
            sumCos += frequencyBin.magnitude * cos(angle.radians.toFloat())
            sumSin += frequencyBin.magnitude * sin(angle.radians.toFloat())
        }

        val radians = atan2(sumSin, sumCos)
        return Angle.fromRadians(radians)
    }

}