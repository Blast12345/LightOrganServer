package sound.input.samples

import javax.sound.sampled.AudioFormat

interface NormalizedAudioFrameFactoryInterface {
    fun create(rawSamples: ByteArray, format: AudioFormat): NormalizedAudioFrame
}

class NormalizedAudioFrameFactory(private val sampleNormalizer: SampleNormalizerInterface = SampleNormalizer()) :
    NormalizedAudioFrameFactoryInterface {

    override fun create(rawSamples: ByteArray, format: AudioFormat): NormalizedAudioFrame {
        return NormalizedAudioFrame(
            samples = getNormalizedSamples(rawSamples, format),
            sampleRate = getSampleRate(format)
        )
    }

    private fun getNormalizedSamples(rawSamples: ByteArray, format: AudioFormat): DoubleArray {
        return sampleNormalizer.normalize(rawSamples, format)
    }

    private fun getSampleRate(format: AudioFormat): Float {
        return format.sampleRate
    }

}