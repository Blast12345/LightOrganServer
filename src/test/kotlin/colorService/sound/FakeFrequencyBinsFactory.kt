package colorService.sound

class FakeFrequencyBinsFactory: FrequencyBinsFactoryInterface {

    var sampleRate: Int? = null
    var sampleSize: Int? = null
    var amplitudes: DoubleArray? = null
    val frequencyBins = listOf(FrequencyBin(1.0, 1.0))

    override fun createFrom(sampleRate: Int, sampleSize: Int, amplitudes: DoubleArray): List<FrequencyBin> {
        this.sampleRate = sampleRate
        this.sampleSize = sampleSize
        this.amplitudes = amplitudes
        return frequencyBins
    }

}