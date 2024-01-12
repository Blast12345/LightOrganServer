package sound.signalProcessor.interpolator

class ZeroPaddingInterpolationAlgorithm {

    fun applyTo(
        samples: DoubleArray,
        desiredSize: Int
    ): DoubleArray {
        val sizeDifference = desiredSize - samples.size

        if (sizeDifference >= 0) {
            return samples + DoubleArray(sizeDifference)
        } else {
            throw InterpolationSizeTooSmallException()
        }
    }

}
