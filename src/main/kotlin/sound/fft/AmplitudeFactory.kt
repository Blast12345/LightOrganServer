package sound.fft

interface AmplitudeFactoryInterface {
    fun create(signal: DoubleArray): DoubleArray
}

class AmplitudeFactory(
    private val hannWindowFilter: HannWindowFilterInterface = HannWindowFilter(),
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm()
) : AmplitudeFactoryInterface {

    override fun create(signal: DoubleArray): DoubleArray {
        val filteredSignal = hannWindowFilter.filter(signal)
        return fftAlgorithm.process(filteredSignal)
    }

}