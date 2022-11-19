package colorListener.sound.fft

class FakeHannWindowFilter : HannWindowFilterInterface {

    var signal: DoubleArray? = null
    val filteredSignal: DoubleArray = DoubleArray(3)

    override fun filter(signal: DoubleArray): DoubleArray {
        this.signal = signal
        return filteredSignal
    }

}