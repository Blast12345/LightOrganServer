package lightOrgan.sound.frequencyBins

interface NyquistFrequencyCalculatorInterface {
    fun calculate(sampleRate: Float): Float
}

class NyquistFrequencyCalculator : NyquistFrequencyCalculatorInterface {

    override fun calculate(sampleRate: Float): Float {
        return sampleRate / 2
    }

}