package sound.signalProcessing

import input.audioFrame.AudioFrame

class SampleExtractor {

    fun extract(audioFrame: AudioFrame, sampleSize: Int): DoubleArray {
        val latestSamples = audioFrame.samples.takeLast(sampleSize)
        return latestSamples.toDoubleArray()
    }

}