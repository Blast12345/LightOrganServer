package input

import input.audioFrame.AudioFrame
import input.audioFrame.AudioFrameFactory
import input.buffer.InputBuffer
import input.lineListener.LineListener
import input.lineListener.LineListenerSubscriber
import javax.sound.sampled.AudioFormat

class Input(
    private val subscribers: MutableSet<InputSubscriber> = mutableSetOf(),
    private val lineListener: LineListener,
    private val buffer: InputBuffer,
    private val audioFrameFactory: AudioFrameFactory = AudioFrameFactory()
) : LineListenerSubscriber {

    val audioFormat: AudioFormat
        get() = lineListener.audioFormat

    init {
        lineListener.subscribers.add(this)
    }

    fun addSubscriber(subscriber: InputSubscriber) {
        subscribers.add(subscriber)
    }

    fun removeSubscriber(subscriber: InputSubscriber) {
        subscribers.remove(subscriber)
    }

    fun checkIfSubscribed(subscriber: InputSubscriber): Boolean {
        return subscribers.contains(subscriber)
    }

    override fun received(newSamples: ByteArray) {
        val audioFrame = getAudioFrameForUpdatedBuffer(newSamples)
        giveAudioFrameToSubscribers(audioFrame)
    }

    private fun getAudioFrameForUpdatedBuffer(newSamples: ByteArray): AudioFrame {
        return audioFrameFactory.create(
            samples = buffer.updatedWith(newSamples),
            format = audioFormat
        )
    }

    private fun giveAudioFrameToSubscribers(audioFrame: AudioFrame) {
        subscribers.forEach {
            it.received(audioFrame)
        }
    }

}