package sound.bins.frequency.listCalculator

import sound.signalProcessor.fft.RelativeMagnitudeListCalculator
import sound.signalProcessor.fft.RelativeMagnitudeListNormalizer

class MagnitudeListCalculator(
    private val relativeMagnitudeListCalculator: RelativeMagnitudeListCalculator = RelativeMagnitudeListCalculator(),
    private val relativeMagnitudeListNormalizer: RelativeMagnitudeListNormalizer = RelativeMagnitudeListNormalizer()
) {

    fun calculate(samples: DoubleArray): DoubleArray {
        val relativeMagnitudeList = getRelativeMagnitudeList(samples)
        return getNormalizedMagnitudes(relativeMagnitudeList, samples.size)
    }

    private fun getRelativeMagnitudeList(processedSamples: DoubleArray): DoubleArray {
        return relativeMagnitudeListCalculator.calculate(processedSamples)
    }

    private fun getNormalizedMagnitudes(relativeMagnitudeList: DoubleArray, processedSampleSize: Int): DoubleArray {
        return relativeMagnitudeListNormalizer.normalize(relativeMagnitudeList, processedSampleSize)
    }

}
