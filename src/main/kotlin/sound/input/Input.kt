package sound.input

import sound.input.samples.NormalizedAudioFrame
import sound.input.samples.NormalizedAudioFrameFactory
import sound.input.samples.NormalizedAudioFrameFactoryInterface
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

interface InputInterface {
    fun listenForAudioSamples(delegate: InputDelegate)
}

class Input(
    private val dataLine: TargetDataLine,
    private val audioFrameFactory: NormalizedAudioFrameFactoryInterface = NormalizedAudioFrameFactory()
) : InputInterface {

    private var samplesBuffer = ByteArray(dataLine.bufferSize)
    private var shouldListen: Boolean = false

    init {
        dataLine.open()
        dataLine.start()
    }

    override fun listenForAudioSamples(delegate: InputDelegate) {
        shouldListen = true
        watchForNewData(delegate)
    }

    private fun watchForNewData(delegate: InputDelegate) {
        // NOTE: New data becomes available every ~10 ms. I don't know how this delay is derived.
        while (shouldListen) {
            if (hasDataAvailable()) {
                returnNextSampleTo(delegate)
            }
        }
    }

    private fun hasDataAvailable(): Boolean {
        return dataLine.available() > 0
    }

    private fun returnNextSampleTo(delegate: InputDelegate) {
        val nextAudioFrame = getNextAudioFrame()
        delegate.receiveAudioFrame(nextAudioFrame)
    }

    private fun getNextAudioFrame(): NormalizedAudioFrame {
        return audioFrameFactory.create(
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
        val bytesToRead = dataLine.available()
        val newData = ByteArray(bytesToRead)
        // TODO: Do the bytes read match the new data size? We need to know for the drop function.
        val bytesRead = dataLine.read(newData, 0, newData.size)
        return newData
    }

    private fun updateBufferWith(newData: ByteArray) {
        val rolloverData = samplesBuffer.drop(newData.size).toByteArray()
        samplesBuffer = rolloverData + newData
    }

    private fun getAudioFormat(): AudioFormat {
        return dataLine.format
    }

    fun stopListening() {
        shouldListen = false
    }

}

