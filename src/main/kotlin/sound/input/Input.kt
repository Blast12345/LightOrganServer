package sound.input

import sound.input.samples.AudioSignalFactory
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class Input(
    dataLine: TargetDataLine,
    private var delegate: InputDelegate? = null,
    targetDataLineListener: TargetDataLineListener = TargetDataLineListener(dataLine),
    private val buffer: AudioBuffer = AudioBuffer(dataLine.bufferSize),
    private val audioClipFactory: AudioSignalFactory = AudioSignalFactory()
) : TargetDataLineListenerDelegate {

    init {
        targetDataLineListener.setDelegate(this)
    }

    fun setDelegate(delegate: InputDelegate) {
        this.delegate = delegate
    }

    fun getDelegate(): InputDelegate? {
        return delegate
    }

    override fun received(newSamples: ByteArray, format: AudioFormat) {
        val updatedSamples = buffer.updatedWith(newSamples)
        val audioClip = audioClipFactory.create(updatedSamples, format)
        delegate?.received(audioClip)
    }

}