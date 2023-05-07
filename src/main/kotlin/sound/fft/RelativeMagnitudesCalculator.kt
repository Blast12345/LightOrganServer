package sound.fft

class RelativeMagnitudesCalculator(
    private val fftAlgorithm: FftAlgorithm = FftAlgorithm(),
    private val magnitudeNormalizer: MagnitudeNormalizer = MagnitudeNormalizer()
) {

    fun calculate(signal: DoubleArray): DoubleArray {
        val magnitudes = fftAlgorithm.calculateRelativeMagnitudes(signal)
        return magnitudeNormalizer.normalize(magnitudes, signal.size)
    }

}