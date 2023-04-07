package lightOrgan.sound.fft

interface RelativeMagnitudesCalculatorInterface {
    fun calculate(signal: DoubleArray): DoubleArray
}

class RelativeMagnitudesCalculator(
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm(),
    private val magnitudeNormalizer: MagnitudeNormalizerInterface = MagnitudeNormalizer()
) : RelativeMagnitudesCalculatorInterface {

    override fun calculate(signal: DoubleArray): DoubleArray {
        val magnitudes = fftAlgorithm.calculateRelativeMagnitudes(signal)
        return magnitudeNormalizer.normalize(magnitudes, signal.size)
    }

}