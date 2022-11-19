package colorListener.sound.frequencyBins

interface FrequencyBinsFactoryInterface {
    fun createFrom(sampleRate: Int, sampleSize: Int, amplitudes: DoubleArray): List<FrequencyBin>
}

class FrequencyBinsFactory : FrequencyBinsFactoryInterface {

    override fun createFrom(sampleRate: Int, sampleSize: Int, amplitudes: DoubleArray): List<FrequencyBin> {
        val frequencyBins = mutableListOf<FrequencyBin>()

        for (i in amplitudes.indices) {
            val frequency = i * sampleRate.toDouble() / sampleSize.toDouble()
            val amplitude = amplitudes[i]
            val frequencyBin = FrequencyBin(frequency, amplitude)
            frequencyBins.add(frequencyBin)
        }

        return frequencyBins
    }

}