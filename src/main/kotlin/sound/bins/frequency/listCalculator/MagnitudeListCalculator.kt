package sound.bins.frequency.listCalculator

import sound.signalProcessor.SignalProcessor
import sound.signalProcessor.fft.RelativeMagnitudeListCalculator
import sound.signalProcessor.fft.RelativeMagnitudeListNormalizer

class MagnitudeListCalculator(
    private val signalProcessor: SignalProcessor = SignalProcessor(),
    private val relativeMagnitudeListCalculator: RelativeMagnitudeListCalculator = RelativeMagnitudeListCalculator(),
    private val relativeMagnitudeListNormalizer: RelativeMagnitudeListNormalizer = RelativeMagnitudeListNormalizer()
) {

    fun calculate(samples: DoubleArray): DoubleArray {
        val processedSamples = getProcessedSamples(samples)
        val relativeMagnitudeList = getRelativeMagnitudeList(processedSamples)
        return getNormalizedMagnitudes(relativeMagnitudeList, processedSamples.size)
    }

    private fun getProcessedSamples(samples: DoubleArray): DoubleArray {
        return signalProcessor.process(samples)
    }

    private fun getRelativeMagnitudeList(processedSamples: DoubleArray): DoubleArray {
        return relativeMagnitudeListCalculator.calculate(processedSamples)
    }

    private fun getNormalizedMagnitudes(relativeMagnitudeList: DoubleArray, processedSampleSize: Int): DoubleArray {
        return relativeMagnitudeListNormalizer.normalize(relativeMagnitudeList, processedSampleSize)
    }

}