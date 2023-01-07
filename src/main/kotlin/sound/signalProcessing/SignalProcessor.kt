package sound.signalProcessing

import sound.input.samples.AudioSignal

interface SignalProcessorInterface {
    fun process(audioSignal: AudioSignal, lowestFrequency: Float): DoubleArray
}

class SignalProcessor : SignalProcessorInterface {

    override fun process(audioSignal: AudioSignal, lowestFrequency: Float): DoubleArray {
        TODO("Not yet implemented")
    }

}