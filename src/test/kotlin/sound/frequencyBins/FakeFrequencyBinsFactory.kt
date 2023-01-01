package sound.frequencyBins

import sound.input.sample.AudioFrame

class FakeFrequencyBinsFactory : FrequencyBinsFactoryInterface {

    var audioFrame: AudioFrame? = null
    var lowestSupportedFrequency: Float? = null
    val frequencyBins = listOf(FrequencyBin(1.0, 1.0))

    override fun createFrom(audioFrame: AudioFrame, lowestSupportedFrequency: Float): List<FrequencyBin> {
        this.audioFrame = audioFrame
        this.lowestSupportedFrequency = lowestSupportedFrequency
        return frequencyBins
    }

}