package sound.signalProcessing

interface DenoiserInterface {
    fun denoise(magnitudes: DoubleArray): DoubleArray
}

class Denoiser() : DenoiserInterface {

    override fun denoise(magnitudes: DoubleArray): DoubleArray {
        val denoisedMagnitudes = getDenoisedMagnitudes(magnitudes)
        return denoisedMagnitudes.toDoubleArray()
    }

    private fun getDenoisedMagnitudes(magnitudes: DoubleArray): List<Double> {
        return magnitudes.map { magnitude ->
            getDenoisedMagnitude(magnitude)
        }
    }

    private fun getDenoisedMagnitude(magnitude: Double): Double {
        val denoisedMagnitude = magnitude - 0.001

        return if (denoisedMagnitude > 0) {
            denoisedMagnitude
        } else {
            0.0
        }
    }

}