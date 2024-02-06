package sound.signalProcessor

import sound.signalProcessor.fft.RelativeMagnitudeListCalculator
import sound.signalProcessor.fft.RelativeMagnitudeListNormalizer

// TODO: This name doesn't feel right
class WindowsProcessor(
    private val relativeMagnitudeListCalculator: RelativeMagnitudeListCalculator = RelativeMagnitudeListCalculator(),
    private val relativeMagnitudeListNormalizer: RelativeMagnitudeListNormalizer = RelativeMagnitudeListNormalizer()
) {

    // TODO: Test me
    fun process(windows: List<DoubleArray>): DoubleArray {
        val compositeMagnitudes = DoubleArray(windows.first().size / 2)

        for (window in windows) {
            val magnitudesForWindow = relativeMagnitudeListCalculator.calculate(window)

            for (i in compositeMagnitudes.indices) {
                compositeMagnitudes[i] += magnitudesForWindow[i]
            }
        }

        for (i in compositeMagnitudes.indices) {
            compositeMagnitudes[i] = compositeMagnitudes[i] / windows.size
        }

        return relativeMagnitudeListNormalizer.normalize(compositeMagnitudes, compositeMagnitudes.size)
    }
}
