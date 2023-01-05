package sound.signalProcessing

interface DenoiserInterface {
    fun denoise(amplitudes: DoubleArray): DoubleArray
}

class Denoiser() : DenoiserInterface {

    override fun denoise(amplitudes: DoubleArray): DoubleArray {
        val denoisedAmplitudes = getDenoisedAmplitudes(amplitudes)
        return denoisedAmplitudes.toDoubleArray()
    }

    private fun getDenoisedAmplitudes(amplitudes: DoubleArray): List<Double> {
        return amplitudes.map { amplitude ->
            getDenoisedAmplitude(amplitude)
        }
    }

    private fun getDenoisedAmplitude(amplitude: Double): Double {
        val denoisedAmplitude = amplitude - 0.001

        return if (denoisedAmplitude > 0) {
            denoisedAmplitude
        } else {
            0.0
        }
    }

}