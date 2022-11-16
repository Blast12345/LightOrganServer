package colorService.sound

typealias NextFftData = (sampleRate: Int, sampleSize: Int, amplitudes: DoubleArray) -> Unit

interface FftServiceInterface {
    fun listenForFftData(lambda: NextFftData)
}

class FftService(): FftServiceInterface {

    // TODO: Test
    override fun listenForFftData(lambda: NextFftData) {
        // TODO:
    }
}