package sound.signalProcessing

import sound.input.samples.AudioSignal

interface SampleExtractorInterface {
    fun extract(signal: AudioSignal, lowestFrequency: Float): DoubleArray
}

class SampleExtractor(
    private val sampleSizeCalculator: SampleSizeCalculatorInterface = SampleSizeCalculator()
) : SampleExtractorInterface {

    override fun extract(signal: AudioSignal, lowestFrequency: Float): DoubleArray {
        val newSampleSize = getNewSampleSize(signal, lowestFrequency)
        return getLatestSamples(signal, newSampleSize)
    }

    private fun getNewSampleSize(signal: AudioSignal, lowestFrequency: Float): Int {
        return sampleSizeCalculator.calculate(lowestFrequency, signal.sampleRate)
    }
    
    private fun getLatestSamples(signal: AudioSignal, sampleSize: Int): DoubleArray {
        val latestSamples = signal.samples.takeLast(sampleSize)
        return latestSamples.toDoubleArray()
    }

}