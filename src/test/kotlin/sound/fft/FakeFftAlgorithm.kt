package sound.fft

import sound.fft.FftAlgorithmInterface

class FakeFftAlgorithm : FftAlgorithmInterface {

    var signal: DoubleArray? = null
    val amplitudes = DoubleArray(7)

    override fun process(signal: DoubleArray): DoubleArray {
        this.signal = signal
        return amplitudes
    }

}