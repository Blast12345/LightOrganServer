package sound.signalProcessor.interpolator

import config.ConfigSingleton

class ZeroPaddingInterpolator(
    private val algorithm: ZeroPaddingInterpolationAlgorithm = ZeroPaddingInterpolationAlgorithm(),
    private val corrector: ZeroPaddingInterpolationCorrector = ZeroPaddingInterpolationCorrector(),
    private val desiredSize: Int = ConfigSingleton.interpolatedSampleSize
) {

    fun interpolate(samples: DoubleArray): DoubleArray {
        return samples
            .applyAlgorithm()
            .applyCorrection(originalSize = samples.size)
    }

    private fun DoubleArray.applyAlgorithm(): DoubleArray {
        return algorithm.applyTo(
            samples = this,
            desiredSize = desiredSize
        )
    }

    private fun DoubleArray.applyCorrection(originalSize: Int): DoubleArray {
        return corrector.correct(
            samples = this,
            originalSize = originalSize
        )
    }

}