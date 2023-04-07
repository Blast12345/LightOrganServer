package lightOrgan.sound.input

import lightOrgan.sound.input.samples.AudioSignalFactory
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class Input(
    val dataLine: TargetDataLine,
    val listeners: MutableSet<InputDelegate> = mutableSetOf(),
    targetDataLineListener: TargetDataLineListener = TargetDataLineListener(dataLine = dataLine),
    private val buffer: AudioBuffer = AudioBuffer(dataLine.bufferSize),
    private val audioClipFactory: AudioSignalFactory = AudioSignalFactory()
) : TargetDataLineListenerDelegate {

    init {
        targetDataLineListener.listeners.add(this)
    }

    override fun received(newSamples: ByteArray, format: AudioFormat) {
        val updatedSamples = buffer.updatedWith(newSamples)
        val audioClip = audioClipFactory.create(updatedSamples, format)

        listeners.forEach {
            it.received(audioClip)
        }
    }

}