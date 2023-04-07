package lightOrgan.sound.signalProcessing.zeroPaddingInterpolator

class NormalizedZeroPaddingInterpolator(
    private val interpolator: ZeroPaddingInterpolatorInterface = ZeroPaddingInterpolator(),
    private val interpolatorNormalizer: ZeroPaddingInterpolatorNormalizerInterface = ZeroPaddingInterpolatorNormalizer()
) : ZeroPaddingInterpolatorInterface {

    override fun interpolate(samples: DoubleArray, desiredSize: Int): DoubleArray {
        val interpolatedSamples = interpolator.interpolate(samples, desiredSize)
        return interpolatorNormalizer.normalize(interpolatedSamples, samples.size)
    }

}