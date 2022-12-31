package sound.frequencyBins

interface FrequencyBinsFactoryInterface {
    fun createFrom(sampleRate: Int, sampleSize: Int, amplitudes: DoubleArray): List<FrequencyBin>
}

class FrequencyBinsFactory : FrequencyBinsFactoryInterface {

    override fun createFrom(sampleRate: Int, sampleSize: Int, amplitudes: DoubleArray): List<FrequencyBin> {
        val frequencyBins = mutableListOf<FrequencyBin>()

        for (i in amplitudes.indices) {
            // TODO: Frequency may need to be divided by 2 in order to get the Nyquist Frequency
            // https://www.mixinglessons.com/sample-rate/
            // TODO: Why times two? To make up for the halving of amplitudes?
            val frequency = i * 2.0//sampleRate.toDouble() / sampleSize.toDouble()
            val amplitude = amplitudes[i]
            val frequencyBin = FrequencyBin(frequency, amplitude)
            frequencyBins.add(frequencyBin)
        }

        return frequencyBins
    }

}