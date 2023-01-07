package sound.signalProcessing

interface MagnitudeFactoryInterface {
    fun create(samples: DoubleArray): DoubleArray
}

class MagnitudeFactory(
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm(),
    private val hannWindowFilter: HannWindowFilterInterface = HannWindowFilter(),
    private val denoiser: DenoiserInterface = Denoiser()
) : MagnitudeFactoryInterface {

    override fun create(samples: DoubleArray): DoubleArray {
        val hannWindowOutput = hannWindowFilter.filter(samples)
        val fftOutput = fftAlgorithm.calculateMagnitudes(hannWindowOutput)
        return denoiser.denoise(fftOutput)
    }

}