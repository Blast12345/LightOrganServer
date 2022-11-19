package colorListener.sound.fft

class FakeAmplitudeFactory : AmplitudeFactoryInterface {

    var signal: DoubleArray? = null
    val amplitudes = DoubleArray(9)

    override fun createFrom(signal: DoubleArray): DoubleArray {
        this.signal = signal
        return amplitudes
    }

}