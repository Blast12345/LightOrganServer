package input

import input.samples.AudioSignalFactory
import javax.sound.sampled.AudioFormat

// TODO: Create an input factory?
//dataLine: TargetDataLine,
//private val targetDataLineListener: TargetDataLineListener = TargetDataLineListener(dataLine = dataLine),
//private val buffer: AudioBuffer = AudioBuffer(dataLine.bufferSize),

class Input(
    targetDataLineListener: TargetDataLineListener,
    private val buffer: AudioBuffer,
    private val audioSignalFactory: AudioSignalFactory = AudioSignalFactory()
) : TargetDataLineListenerDelegate {

    val listeners: MutableSet<InputDelegate> = mutableSetOf()
    val audioFormat: AudioFormat = targetDataLineListener.audioFormat

    init {
        targetDataLineListener.listeners.add(this)
    }

    override fun received(newSamples: ByteArray) {
        val updatedSamples = buffer.updatedWith(newSamples)
        val audioSignal = audioSignalFactory.create(updatedSamples, audioFormat)

        listeners.forEach {
            it.received(audioSignal)
        }
    }


}