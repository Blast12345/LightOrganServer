package sound.fft

interface AmplitudeFactoryInterface {
    fun create(samples: DoubleArray): DoubleArray
}

class AmplitudeFactory(
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm(),
    private val hannWindowFilter: HannWindowFilterInterface = HannWindowFilter()
) : AmplitudeFactoryInterface {

    override fun create(samples: DoubleArray): DoubleArray {
        val filteredSignal = hannWindowFilter.filter(samples)
        return fftAlgorithm.process(filteredSignal)
    }

}