package sound.signalProcessing

interface MagnitudeNormalizerInterface {
    fun normalize(magnitudes: DoubleArray, sampleSize: Int): DoubleArray
}

class MagnitudeNormalizer: MagnitudeNormalizerInterface {

    override fun normalize(magnitudes: DoubleArray, sampleSize: Int): DoubleArray {
        TODO("Not yet implemented")
    }

}