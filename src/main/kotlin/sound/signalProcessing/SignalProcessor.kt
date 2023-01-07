package sound.signalProcessing

import sound.input.samples.AudioSignal

interface SignalProcessorInterface {
    fun process(audioSignal: AudioSignal): DoubleArray
}

class SignalProcessor : SignalProcessorInterface {

    override fun process(audioSignal: AudioSignal): DoubleArray {
        TODO("Not yet implemented")
    }

}