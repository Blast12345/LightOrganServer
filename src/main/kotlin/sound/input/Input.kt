package sound.input

import kotlinx.coroutines.*
import sound.input.samples.AudioSignal
import sound.input.samples.AudioSignalFactory
import sound.input.samples.AudioSignalFactoryInterface
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

interface InputInterface {
    fun listenForAudio(delegate: InputDelegate)
}

class Input(
    private val dataLine: TargetDataLine,
    private val audioSignalFactory: AudioSignalFactoryInterface = AudioSignalFactory(),
    private val scope: CoroutineScope = MainScope(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : InputInterface {

    private var samplesBuffer = ByteArray(dataLine.bufferSize)
    private var job: Job? = null

    init {
        dataLine.open()
        dataLine.start()
    }

    override fun listenForAudio(delegate: InputDelegate) {
        job = scope.launch(dispatcher) {
            while (isActive) {
                checkForNewAudioSignal(delegate)
                delay(1) // TODO: Compute the delay based on the average time between data becoming available
            }
        }
    }

    private fun checkForNewAudioSignal(delegate: InputDelegate) {
        if (hasDataAvailable()) {
            returnNextSampleTo(delegate)
        }
    }

    private fun hasDataAvailable(): Boolean {
        return dataLine.available() > 0
    }

    private fun returnNextSampleTo(delegate: InputDelegate) {
        val newAudioSignal = getNextAudioSignal()
        delegate.receivedAudio(newAudioSignal)
    }

    private fun getNextAudioSignal(): AudioSignal {
        return audioSignalFactory.create(
            samples = getNewestSamples(),
            format = getAudioFormat()
        )
    }

    private fun getNewestSamples(): ByteArray {
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
        job?.cancel()
    }

}

