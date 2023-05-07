package sound.signalProcessing.zeroPaddingInterpolator

class NormalizedZeroPaddingInterpolator(
    private val interpolator: StandardZeroPaddingInterpolator = StandardZeroPaddingInterpolator(),
    private val interpolatorNormalizer: ZeroPaddingInterpolatorNormalizer = ZeroPaddingInterpolatorNormalizer()
) : ZeroPaddingInterpolator {

    override fun interpolate(samples: DoubleArray, desiredSize: Int): DoubleArray {
        val interpolatedSamples = interpolator.interpolate(samples, desiredSize)
        return interpolatorNormalizer.normalize(interpolatedSamples, samples.size)
    }

}