package sound.signalProcessing.zeroPaddingInterpolator

class ZeroPaddingInterpolatorNormalizer {

    fun normalize(interpolatedSamples: DoubleArray, originalSize: Int): DoubleArray {
        val percentOfOriginalSamples = calculatePercentOfOriginalSamples(interpolatedSamples, originalSize)

        return interpolatedSamples.map {
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