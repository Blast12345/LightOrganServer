package lightOrgan.sound.signalProcessing

import input.audioFrame.AudioFrame

interface SampleExtractorInterface {
    fun extract(signal: AudioFrame, sampleSize: Int): DoubleArray
}

class SampleExtractor() : SampleExtractorInterface {

    override fun extract(signal: AudioFrame, sampleSize: Int): DoubleArray {
        val latestSamples = signal.samples.takeLast(sampleSize)
        return latestSamples.toDoubleArray()
    }

}