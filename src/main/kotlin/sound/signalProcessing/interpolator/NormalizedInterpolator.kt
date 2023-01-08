package sound.signalProcessing.interpolator

class NormalizedInterpolator(
    private val interpolator: InterpolatorInterface = Interpolator(),
    private val interpolatorNormalizer: InterpolatorNormalizerInterface = InterpolatorNormalizer()
) : InterpolatorInterface {

    override fun interpolate(samples: DoubleArray, desiredSize: Int): DoubleArray {
        val interpolatedSamples = interpolator.interpolate(samples, desiredSize)
        return interpolatorNormalizer.normalize(interpolatedSamples, samples.size)
    }

}