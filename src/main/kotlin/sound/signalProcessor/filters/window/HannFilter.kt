package sound.signalProcessor.filters.window

class HannFilter(
    private val algorithm: HannFilterAlgorithm = HannFilterAlgorithm(),
    private val corrector: HannFilterCorrector = HannFilterCorrector(),
) : WindowFilter {

    override fun applyTo(samples: DoubleArray): DoubleArray {
        return samples
            .applyAlgorithm()
            .applyCorrection()
    }

    private fun DoubleArray.applyAlgorithm(): DoubleArray {
        return algorithm.applyTo(this)
    }

    private fun DoubleArray.applyCorrection(): DoubleArray {
        return corrector.correct(this)
    }

}