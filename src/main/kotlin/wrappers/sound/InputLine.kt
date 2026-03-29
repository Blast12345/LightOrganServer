package wrappers.sound

import logging.Logger
import java.nio.ByteOrder
import javax.sound.sampled.TargetDataLine

// NOTE: Buffer size is temperamental.
// Very low buffers (e.g., 128) result in seemingly random data, likely from data loss via overflow.
// Low buffers (e.g., 2048) result in occasional hiccups, likely from infrequent data loss via overlow.
// High buffers (e.g., 96,000) result in low update rates, likely from the audio driver switching to larger batch sizes (e.g., 26,624).
// The sweet spot likely depends on the operating system and input device.
class InputLine(
    val name: String,
    private val dataLine: TargetDataLine,
    private val minimumReadSize: Int = 2048, // ENHANCEMENT: Make the minimum read size configurable in the GUI.
    private val bufferSize: Int = 8192 // ENHANCEMENT: Make the buffer size configurable in the GUI.
) {

    val sampleRate = dataLine.format.sampleRate
    val bitDepth = dataLine.format.sampleSizeInBits
    val channels = dataLine.format.channels
    val byteOrder: ByteOrder = if (dataLine.format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN

    init {
        require(bufferSize >= minimumReadSize) { "Minimum read size ($minimumReadSize) must not exceed buffer size ($bufferSize)" }
    }

    // Lifecycle
    fun start() {
        try {
            dataLine.open(dataLine.format, bufferSize)
            dataLine.start()
        } catch (e: Exception) {
            stop()
            throw e
        }
    }

    fun stop() {
        dataLine.stop()
        dataLine.close()
    }

    // Reading
    fun read(): ReadResult {
        val available = dataLine.available()
        val readSize = if (available > minimumReadSize) available else minimumReadSize
        val bufferWasFull = available >= bufferSize

        val readData = ByteArray(readSize)
        val lengthRead = dataLine.read(readData, 0, readSize) // This will block until readSize bytes are available

        if (lengthRead != readSize) {
            Logger.warning("Read $lengthRead bytes instead of $readSize bytes")
        }

        return ReadResult(readData, bufferWasFull)
    }

    class ReadResult(
        val data: ByteArray,
        val bufferWasFull: Boolean
    )

}