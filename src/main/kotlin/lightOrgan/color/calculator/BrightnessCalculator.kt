package lightOrgan.color.calculator

import bins.FrequencyBin
import bins.FrequencyBins
import bins.PeakFrequencyBinsCalculator
import music.Notes
import utilities.TimestampUtility
import kotlin.math.log2
import kotlin.math.pow

class BrightnessCalculator(
    // NOTE: Remember that high pass is the lower filter and low pass is the higher filter.
    // TODO: Pull these default filters from somewhere?
    private val highPassFilter: HighPassFilter? = HighPassFilter(Notes.C.getFrequency(1), 24f),
    private val lowPassFilter: LowPassFilter? = LowPassFilter(Notes.G.getFrequency(2), 48f),
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
        return lowPassFilter?.trim(this, 48f) ?: this
    }

    private fun FrequencyBins.trimHighPassFilter(): FrequencyBins {
        return highPassFilter?.trim(this, 48f) ?: this
    }

    private fun FrequencyBins.applyLowPassFilter(): FrequencyBins {
        return lowPassFilter?.filter(this) ?: this
    }

    private fun FrequencyBins.applyHighPassFilter(): FrequencyBins {
        return highPassFilter?.filter(this) ?: this
    }

}

// TODO: Test me
class HighPassFilter(
    val frequency: Float,
    val slope: Float
) {

    fun trim(bins: FrequencyBins, thresholdDb: Float): FrequencyBins {
        val hardCutoff = frequency / 2f.pow(thresholdDb / slope)
        return bins.filter { it.frequency >= hardCutoff }
    }

    fun filter(bins: FrequencyBins): FrequencyBins {
        return bins.map { filter(it) }
    }

    private fun filter(bin: FrequencyBin): FrequencyBin {
        val octaves = log2(frequency / bin.frequency)
        if (octaves <= 0f) return bin
        val attenuationDb = slope * octaves
        val multiplier = 10f.pow(-attenuationDb / 20f)
        return bin.copy(magnitude = bin.magnitude * multiplier)
    }

}

class LowPassFilter(
    val frequency: Float,
    val slope: Float
) {

    fun trim(bins: FrequencyBins, thresholdDb: Float): FrequencyBins {
        val hardCutoff = frequency * 2f.pow(thresholdDb / slope)
        return bins.filter { it.frequency <= hardCutoff }
    }

    fun filter(bins: FrequencyBins): FrequencyBins {
        return bins.map { filter(it) }
    }

    private fun filter(bin: FrequencyBin): FrequencyBin {
        val octaves = log2(bin.frequency / frequency)
        if (octaves <= 0f) return bin
        val attenuationDb = slope * octaves
        val multiplier = 10f.pow(-attenuationDb / 20f)
        return bin.copy(magnitude = bin.magnitude * multiplier)
    }

}