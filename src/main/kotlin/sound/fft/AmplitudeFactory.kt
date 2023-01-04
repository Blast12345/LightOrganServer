package sound.fft

interface AmplitudeFactoryInterface {
    fun create(samples: DoubleArray): DoubleArray
}

class AmplitudeFactory(
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm(),
    private val hannWindowFilter: HannWindowFilterInterface = HannWindowFilter()
) : AmplitudeFactoryInterface {

    override fun create(samples: DoubleArray): DoubleArray {
        return getAmplitudes(samples)
    }

    private fun getAmplitudes(samples: DoubleArray): DoubleArray {
        val filteredSignal = filterUsingHannWindow(samples)
        return fftAlgorithm.process(filteredSignal)
    }

    private fun filterUsingHannWindow(samples: DoubleArray): DoubleArray {
        return hannWindowFilter.filter(samples)
    }

}