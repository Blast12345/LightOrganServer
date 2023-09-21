package sound.signalProcessor.fft

class RelativeMagnitudeListNormalizer {

    fun normalize(magnitudes: DoubleArray, sampleSize: Int): DoubleArray {
        return magnitudes.map { magnitude ->
            normalize(magnitude, sampleSize)
        }.toDoubleArray()
    }

    private fun normalize(magnitude: Double, sampleSize: Int): Double {
        return magnitude / sampleSize
    }

}