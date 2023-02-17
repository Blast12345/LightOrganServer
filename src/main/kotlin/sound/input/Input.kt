package sound.input

import config.Config
import sound.input.samples.AudioSignalFactory
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class Input : TargetDataLineListenerDelegate {

    private val targetDataLineListener: TargetDataLineListener
    private val buffer: AudioBuffer
    private val audioClipFactory: AudioSignalFactory
    private val delegate: InputDelegate

    constructor(
        targetDataLineListener: TargetDataLineListener,
        buffer: AudioBuffer,
        audioClipFactory: AudioSignalFactory,
        delegate: InputDelegate
    ) {
        this.targetDataLineListener = targetDataLineListener
        this.buffer = buffer
        this.audioClipFactory = audioClipFactory
        this.delegate = delegate
    }

    constructor(
        dataLine: TargetDataLine,
        config: Config,
        delegate: InputDelegate
    ) {
        this.targetDataLineListener = TargetDataLineListener(dataLine, this, config)
        this.buffer = AudioBuffer(dataLine.bufferSize)
        this.audioClipFactory = AudioSignalFactory()
        this.delegate = delegate
    }

    override fun received(newSamples: ByteArray, format: AudioFormat) {
        val updatedSamples = buffer.updatedWith(newSamples)
        val audioClip = audioClipFactory.create(updatedSamples, format)
        delegate.received(audioClip)
    }

}