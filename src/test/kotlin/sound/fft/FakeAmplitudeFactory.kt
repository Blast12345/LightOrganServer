package sound.fft

class FakeAmplitudeFactory : AmplitudeFactoryInterface {

    var signal: DoubleArray? = null
    val amplitudes = DoubleArray(9)

    override fun create(signal: DoubleArray): DoubleArray {
        this.signal = signal
        return amplitudes
    }

}