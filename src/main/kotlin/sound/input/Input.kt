package sound.input

import kotlinx.coroutines.delay
import sound.input.samples.NormalizedAudioFrame
import sound.input.samples.NormalizedAudioFrameFactory
import sound.input.samples.NormalizedAudioFrameFactoryInterface
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

interface InputDelegate {
    fun receiveAudioFrame(audioFrame: NormalizedAudioFrame)
}

interface InputInterface {
    suspend fun listenForAudioSamples(delegate: InputDelegate)
}

class Input(
    private val dataLine: TargetDataLine,
    private val audioFrameFactory: NormalizedAudioFrameFactoryInterface = NormalizedAudioFrameFactory()
) : InputInterface {

    private var samplesBuffer = ByteArray(dataLine.bufferSize)

    override suspend fun listenForAudioSamples(delegate: InputDelegate) {
        startInput()
        watchForNewData(delegate)
    }

    private suspend fun startInput() {
        dataLine.open()
        dataLine.start()

        while (!dataLine.isActive) {
            println("Waiting for input to start...")
            delay(500)
        }
    }

    private fun watchForNewData(delegate: InputDelegate) {
        // NOTE: New data becomes available every ~10 ms. I don't know how this delay is derived.
        while (dataLine.isActive) {
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

}

