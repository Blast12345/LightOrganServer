package sound.bins.frequencyBins.listCalculator

import wrappers.audioFormat.AudioFormatWrapper

class GranularityCalculator {

    fun calculate(numberOfBins: Int, audioFormat: AudioFormatWrapper): Float {
        return calculate(
            nyquistFrequency = audioFormat.nyquistFrequency,
            numberOfBins = numberOfBins,
            numberOfChannels = audioFormat.numberOfChannels
        )
    }

    private fun calculate(nyquistFrequency: Float, numberOfBins: Int, numberOfChannels: Int): Float {
        return nyquistFrequency / numberOfBins * numberOfChannels
    }

}