package sound.bins.frequency.listCalculator

import config.ConfigSingleton
import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.FrequencyBinsFactory
import wrappers.audioFormat.AudioFormatWrapper

class FrequencyBinsCalculator(
    private val magnitudeListCalculator: MagnitudeListCalculator = MagnitudeListCalculator(),
    private val granularityCalculator: GranularityCalculator = GranularityCalculator(),
    private val frequencyBinsFactory: FrequencyBinsFactory = FrequencyBinsFactory()
) {

    fun calculate(samples: DoubleArray, audioFormat: AudioFormatWrapper): FrequencyBins {
        val magnitudes = getMagnitudes(samples, audioFormat)
        val granularity = ConfigSingleton.step.toFloat()//getGranularity(magnitudes, audioFormat) / ConfigSingleton.decimationFactor

        return frequencyBinsFactory
            .create(magnitudes, granularity)
            .removeBinsBeyondNyquistFrequency(audioFormat)
    }

    private fun getMagnitudes(samples: DoubleArray, format: AudioFormatWrapper): DoubleArray {
        return magnitudeListCalculator.calculateNew(samples, format)
    }

    private fun getGranularity(magnitudes: DoubleArray, audioFormat: AudioFormatWrapper): Float {
        return granularityCalculator.calculate(
            numberOfBins = magnitudes.size,
            audioFormat = audioFormat
        )
    }

    private fun FrequencyBins.removeBinsBeyondNyquistFrequency(audioFormat: AudioFormatWrapper): FrequencyBins {
        return this.removeBinsBeyondNyquistFrequency(audioFormat.nyquistFrequency)
    }

    private fun FrequencyBins.removeBinsBeyondNyquistFrequency(nyquistFrequency: Float): FrequencyBins {
        return this.filter { it.frequency <= nyquistFrequency }
    }

}
