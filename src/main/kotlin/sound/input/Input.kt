package sound.input

import javax.sound.sampled.TargetDataLine

typealias NextAudioSample = (signal: DoubleArray) -> Unit

interface InputInterface {
    fun listenForAudioSamples(listener: NextAudioSample)
}

class Input(
    private val line: TargetDataLine,
    private val rawWaveFactory: RawWaveFactoryInterface = RawWaveFactory()
) : InputInterface {

    private var shouldListen = false
    private var buffer = ByteArray(line.bufferSize)

    override fun listenForAudioSamples(listener: NextAudioSample) {
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

    private fun returnNextSampleTo(listener: NextAudioSample) {
        val nextSample = getNextSample()
        listener(nextSample)
    }

    private fun getNextSample(): DoubleArray {
        val newData = getNewData()
        updateBufferWith(newData)
        return rawWaveFactory.rawWaveFrom(buffer, line.format)
    }

    private fun getNewData(): ByteArray {
        val bytesToRead = line.available()
        val newData = ByteArray(bytesToRead)
        // TODO: Do the bytes read match the new data size? We need to know for the drop function.
        val bytesRead = line.read(newData, 0, newData.size)
        return newData
    }

    private fun updateBufferWith(newData: ByteArray) {
        val rolloverData = buffer.drop(newData.size).toByteArray()
        buffer = rolloverData + newData
    }


    fun stopListening() {
        shouldListen = false
    }
    // TODO:
//    fun sampleRate(): Float {
//        return line.format.sampleRate
//    }
//
//    fun sampleSize(): Int {
//        // NOTE: I think frame size is dependent on bit-depth. 8-bit audio would like be 1, 16 would be 2, 24 would be 3, and 32 would be 4
//        return line.bufferSize / line.format.frameSize
//    }
//
//    fun bufferSize(): Int {
//        return line.bufferSize
//    }

}