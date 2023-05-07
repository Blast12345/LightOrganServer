package sound.frequencyBins

class NyquistFrequencyCalculator {

    fun calculate(sampleRate: Float): Float {
        return sampleRate / 2
    }

}