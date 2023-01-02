package sound.frequencyBins

import sound.input.samples.NormalizedAudioFrame

interface FrequencyBinsFactoryInterface {
    fun createFrom(normalizedAudioFrame: NormalizedAudioFrame, lowestSupportedFrequency: Float): FrequencyBins
}

class FrequencyBinsFactory : FrequencyBinsFactoryInterface {

    override fun createFrom(
        normalizedAudioFrame: NormalizedAudioFrame,
        lowestSupportedFrequency: Float
    ): FrequencyBins {
        val bin1 = FrequencyBin(100.0, 1.1)
        val bin2 = FrequencyBin(200.0, 2.0)
        return listOf(bin1, bin2)

//        val frequencyBins = mutableListOf<FrequencyBin>()
//
//        for (i in amplitudes.indices) {
//            // TODO: Frequency may need to be divided by 2 in order to get the Nyquist Frequency
//            // https://www.mixinglessons.com/sample-rate/
//            // TODO: Why times two? To make up for the halving of amplitudes?
//            val frequency = i * 2.0//sampleRate.toDouble() / sampleSize.toDouble()
//            val amplitude = amplitudes[i]
//            val frequencyBin = FrequencyBin(frequency, amplitude)
//            frequencyBins.add(frequencyBin)
//        }
//
//        return frequencyBins
    }

}