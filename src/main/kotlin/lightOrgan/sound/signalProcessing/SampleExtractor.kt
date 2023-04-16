package lightOrgan.sound.signalProcessing

import input.samples.AudioSignal

interface SampleExtractorInterface {
    fun extract(signal: AudioSignal, sampleSize: Int): DoubleArray
}

class SampleExtractor() : SampleExtractorInterface {

    override fun extract(signal: AudioSignal, sampleSize: Int): DoubleArray {
        val latestSamples = signal.samples.takeLast(sampleSize)
        return latestSamples.toDoubleArray()
    }

}