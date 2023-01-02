package sound.input

import sound.input.samples.NormalizedAudioFrame
import sound.input.samples.NormalizedAudioFrameFactory
import sound.input.samples.NormalizedAudioFrameFactoryInterface
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

typealias NextAudioFrame = (audioFrame: NormalizedAudioFrame) -> Unit

interface InputInterface {
    fun listenForAudioSamples(listener: NextAudioFrame)
}

class Input(
    private val line: TargetDataLine,
    private val audioFrameFactory: NormalizedAudioFrameFactoryInterface = NormalizedAudioFrameFactory()
) : InputInterface {

    private var canListen: Boolean
    private var samplesBuffer = ByteArray(line.bufferSize)

    init {
        line.open()
        line.start()
        canListen = true
    }

    override fun listenForAudioSamples(listener: NextAudioFrame) {
        // NOTE: New data becomes available every ~10 ms. I don't know how this delay is derived.
        while (canListen) {
            if (hasDataAvailable()) {
                returnNextSampleTo(listener)
            }
        }
    }

    private fun hasDataAvailable(): Boolean {
        return line.available() > 0
    }

    private fun returnNextSampleTo(listener: NextAudioFrame) {
        val nextAudioFrame = getNextAudioFrame()
        listener(nextAudioFrame)
    }

    private fun getNextAudioFrame(): NormalizedAudioFrame {
        return audioFrameFactory.createFor(
            rawSamples = getNextFrame(),
            format = getAudioFormat()
        )
    }

    private fun getNextFrame(): ByteArray {
        val newData = getNewData()
        updateBufferWith(newData)
        return samplesBuffer
    }

    private fun getNewData(): ByteArray {
        val bytesToRead = line.available()
        val newData = ByteArray(bytesToRead)
        // TODO: Do the bytes read match the new data size? We need to know for the drop function.
        val bytesRead = line.read(newData, 0, newData.size)
        return newData
    }

    private fun updateBufferWith(newData: ByteArray) {
        val rolloverData = samplesBuffer.drop(newData.size).toByteArray()
        samplesBuffer = rolloverData + newData
    }

    private fun getAudioFormat(): AudioFormat {
        return line.format
    }

    fun stopListening() {
        canListen = false
    }

}

