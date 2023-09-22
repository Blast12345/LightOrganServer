package sound.signalProcessor.interpolator

class ZeroPaddingInterpolationCorrector {

    fun correct(samples: DoubleArray, originalSize: Int): DoubleArray {
        val percentOfOriginalSamples = calculatePercentOfOriginalSamples(samples, originalSize)

        return samples.map {
            it / percentOfOriginalSamples
        }.toDoubleArray()
    }

    private fun calculatePercentOfOriginalSamples(interpolatedSamples: DoubleArray, originalSize: Int): Float {
        return calculatePercentOfOriginalSamples(originalSize, interpolatedSamples.size)
    }

    private fun calculatePercentOfOriginalSamples(originalSize: Int, interpolatedSize: Int): Float {
        return originalSize.toFloat() / interpolatedSize.toFloat()
    }

}