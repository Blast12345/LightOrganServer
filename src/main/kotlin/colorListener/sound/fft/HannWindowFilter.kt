package colorListener.sound.fft

interface HannWindowFilterInterface {
    fun filter(signal: DoubleArray): DoubleArray
}

class HannWindowFilter : HannWindowFilterInterface {

    override fun filter(signal: DoubleArray): DoubleArray {
        TODO("Not yet implemented")
    }

}