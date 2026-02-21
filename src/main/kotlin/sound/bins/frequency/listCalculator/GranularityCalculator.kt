package sound.bins.frequency.listCalculator

import input.samples.AudioFormat

class GranularityCalculator {

    fun calculate(numberOfBins: Int, audioFormat: AudioFormat): Float {
        return calculate(
            nyquistFrequency = audioFormat.nyquistFrequency,
            numberOfBins = numberOfBins,
            numberOfChannels = audioFormat.channels
        )
    }

    private fun calculate(nyquistFrequency: Float, numberOfBins: Int, numberOfChannels: Int): Float {
        return nyquistFrequency / numberOfBins * numberOfChannels
    }

}
