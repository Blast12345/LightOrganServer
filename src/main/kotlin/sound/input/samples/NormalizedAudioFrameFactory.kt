package sound.input.samples

import javax.sound.sampled.AudioFormat

interface NormalizedAudioFrameFactoryInterface {
    fun createFor(rawSamples: ByteArray, format: AudioFormat): NormalizedAudioFrame
}

class NormalizedAudioFrameFactory(private val sampleNormalizer: SampleNormalizerInterface = SampleNormalizer()) :
    NormalizedAudioFrameFactoryInterface {

    override fun createFor(rawSamples: ByteArray, format: AudioFormat): NormalizedAudioFrame {
        return NormalizedAudioFrame(
            samples = getNormalizedSamplesFor(rawSamples, format)
        )
    }

    private fun getNormalizedSamplesFor(rawSamples: ByteArray, format: AudioFormat): DoubleArray {
        return sampleNormalizer.normalize(rawSamples, format)
    }

}