package sound.input

import sound.input.sample.AudioFrame
import sound.input.sample.AudioFrameFactory
import sound.input.sample.AudioFrameFactoryInterface
import javax.sound.sampled.TargetDataLine

typealias NextAudioFrame = (audioFrame: AudioFrame) -> Unit

interface InputInterface {
    fun listenForAudioSamples(listener: NextAudioFrame)
}

class Input(
    private val line: TargetDataLine,
    private val audioFrameFactory: AudioFrameFactoryInterface = AudioFrameFactory()
) : InputInterface {

    private var shouldListen = false
    private var samplesBuffer = ByteArray(line.bufferSize)

    override fun listenForAudioSamples(listener: NextAudioFrame) {
        startListening()
        whenDataIsAvailable {
            returnNextSampleTo(listener)
        }
    }

    private fun startListening() {
        line.open()
        line.start()
        shouldListen = true
    }

    private fun whenDataIsAvailable(dataIsAvailable: () -> Unit) {
        // NOTE: New data becomes available every ~10 ms. I don't know how this delay is derived.
        while (shouldListen) {
            if (hasDataAvailable()) {
                dataIsAvailable()
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

    private fun getNextAudioFrame(): AudioFrame {
        return audioFrameFactory.audioFrameFor(
            samples = getNextFrame(),
            line = line
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

    fun stopListening() {
        shouldListen = false
    }

}

