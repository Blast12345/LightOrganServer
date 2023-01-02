package sound.frequencyBins

import sound.input.samples.NormalizedAudioFrame

class FakeFrequencyBinsFactory : FrequencyBinsFactoryInterface {

    var audioFrame: NormalizedAudioFrame? = null
    var lowestSupportedFrequency: Float? = null
    val frequencyBins = listOf(FrequencyBin(1.0, 1.0))

    override fun createFrom(
        normalizedAudioFrame: NormalizedAudioFrame,
        lowestSupportedFrequency: Float
    ): List<FrequencyBin> {
        this.audioFrame = normalizedAudioFrame
        this.lowestSupportedFrequency = lowestSupportedFrequency
        return frequencyBins
    }

}