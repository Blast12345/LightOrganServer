package lightOrgan.sound

import input.audioFrame.AudioFrame

interface SampleExtractorInterface {
    fun extract(audioFrame: AudioFrame, sampleSize: Int): DoubleArray
}

class SampleExtractor() : SampleExtractorInterface {

    override fun extract(audioFrame: AudioFrame, sampleSize: Int): DoubleArray {
        val latestSamples = audioFrame.samples.takeLast(sampleSize)
        return latestSamples.toDoubleArray()
    }

}