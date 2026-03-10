package lightOrgan.color.calculator

import bins.FrequencyBins
import bins.HighPassFilter
import bins.LowPassFilter
import bins.PeakFrequencyBinsCalculator
import config.ConfigSingleton
import utilities.TimestampUtility

class BrightnessCalculator(
    private val highPassFilter: HighPassFilter? = ConfigSingleton.highPassFilter,
    private val lowPassFilter: LowPassFilter? = ConfigSingleton.lowPassFilter,
    private val peakFrequencyBinsCalculator: PeakFrequencyBinsCalculator = PeakFrequencyBinsCalculator(),
) {

    private val timestamper =
        TimestampUtility("brightness time") // TODO: Expand into a comprehensive performance logger. Have a table.

    // TODO: FilterF
    fun calculate(frequencyBins: FrequencyBins): Float? {
        timestamper.reset()

        val trimmedBins = frequencyBins.trimLowPassFilter().trimHighPassFilter()
        val peakBins = peakFrequencyBinsCalculator.calculate(trimmedBins)
        val filteredPeaks = peakBins.applyHighPassFilter().applyLowPassFilter()
        val peakMagnitude = filteredPeaks.maxOfOrNull { it.magnitude.coerceIn(0f, 1f) }

        timestamper.logTimeSinceLast()

        return peakMagnitude
    }

    private fun FrequencyBins.trimLowPassFilter(): FrequencyBins {
        return lowPassFilter?.trim(this) ?: this
    }

    private fun FrequencyBins.trimHighPassFilter(): FrequencyBins {
        return highPassFilter?.trim(this) ?: this
    }

    private fun FrequencyBins.applyLowPassFilter(): FrequencyBins {
        return lowPassFilter?.filter(this) ?: this
    }

    private fun FrequencyBins.applyHighPassFilter(): FrequencyBins {
        return highPassFilter?.filter(this) ?: this
    }

}


