package sound.input

import sound.input.samples.AudioSignal
import sound.input.samples.AudioSignalFactory
import sound.input.samples.AudioSignalFactoryInterface
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

interface InputInterface {
    fun listenForAudioSamples(delegate: InputDelegate)
}

class Input(
    private val dataLine: TargetDataLine,
    private val audioSignalFactory: AudioSignalFactoryInterface = AudioSignalFactory()
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
        val nextAudioSignal = getNextAudioSignal()
        delegate.receiveAudioSignal(nextAudioSignal)
    }

    private fun getNextAudioSignal(): AudioSignal {
        return audioSignalFactory.create(
            samples = getNextFrame(),
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
        dataLine.read(newData, 0, newData.size)
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

