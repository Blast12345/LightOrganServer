package sound.bins.frequency.listCalculator

import sound.bins.frequency.FrequencyBinList
import sound.bins.frequency.FrequencyBinListFactory
import wrappers.audioFormat.AudioFormatWrapper

class FrequencyBinListCalculator(
    private val magnitudeListCalculator: MagnitudeListCalculator = MagnitudeListCalculator(),
    private val granularityCalculator: GranularityCalculator = GranularityCalculator(),
    private val frequencyBinListFactory: FrequencyBinListFactory = FrequencyBinListFactory()
) {

    fun calculate(samples: DoubleArray, audioFormat: AudioFormatWrapper): FrequencyBinList {
        val magnitudes = getMagnitudes(samples)
        val granularity = getGranularity(magnitudes, audioFormat)

        return frequencyBinListFactory
            .create(magnitudes, granularity)
            .removeBinsBeyondNyquistFrequency(audioFormat)
    }

    private fun getMagnitudes(samples: DoubleArray): DoubleArray {
        return magnitudeListCalculator.calculate(samples)
    }

    private fun getGranularity(magnitudes: DoubleArray, audioFormat: AudioFormatWrapper): Float {
        return granularityCalculator.calculate(
            numberOfBins = magnitudes.size,
            audioFormat = audioFormat
        )
    }

    private fun FrequencyBinList.removeBinsBeyondNyquistFrequency(audioFormat: AudioFormatWrapper): FrequencyBinList {
        return this.removeBinsBeyondNyquistFrequency(audioFormat.nyquistFrequency)
    }

    private fun FrequencyBinList.removeBinsBeyondNyquistFrequency(nyquistFrequency: Float): FrequencyBinList {
        return this.filter { it.frequency <= nyquistFrequency }
    }

}
