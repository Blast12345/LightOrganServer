package sound.signalProcessing

interface AmplitudeFactoryInterface {
    fun create(samples: DoubleArray): DoubleArray
}

class AmplitudeFactory(
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm(),
    private val hannWindowFilter: HannWindowFilterInterface = HannWindowFilter(),
    private val denoiser: DenoiserInterface = Denoiser()
) : AmplitudeFactoryInterface {

    override fun create(samples: DoubleArray): DoubleArray {
        val hannWindowOutput = hannWindowFilter.filter(samples)
        val fftOutput = fftAlgorithm.calculateRelativeAmplitudes(hannWindowOutput)
        return denoiser.denoise(fftOutput)
    }

}