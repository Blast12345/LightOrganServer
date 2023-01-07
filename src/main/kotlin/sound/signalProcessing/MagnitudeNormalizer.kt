package sound.signalProcessing

interface MagnitudeNormalizerInterface {
    fun normalize(magnitudes: DoubleArray, sampleSize: Int): DoubleArray
}

class MagnitudeNormalizer : MagnitudeNormalizerInterface {

    override fun normalize(magnitudes: DoubleArray, sampleSize: Int): DoubleArray {
        TODO("Not yet implemented")
    }

    // TODO: Normalize later
    // TODO: Frequency Bin Sanitizer
//    private fun calculateNormalizedMagnitude(real: Double, imaginary: Double, sampleSize: Int): Float {
//        val magnitude = calculateMagnitude(real, imaginary)
//        val normalizedMagnitude = magnitude / sampleSize
//        return normalizedMagnitude.toFloat()
//    }

}