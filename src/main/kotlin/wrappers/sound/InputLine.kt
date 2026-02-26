package wrappers.sound

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    private val minimumReadSize: Int = 2048,
    private val bufferSize: Int = minimumReadSize * 4
) {

    val sampleRate = dataLine.format.sampleRate
    val bitDepth = dataLine.format.sampleSizeInBits
    val channels = dataLine.format.channels
    val byteOrder: ByteOrder = if (dataLine.format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN
    
    fun start() {
        try {
            dataLine.open(dataLine.format, bufferSize)
            dataLine.start()
        } catch (e: Exception) {
            stop()
            throw e
        }
    }

    // ENHANCEMENT: Notify the UI when more data than the readSize is available (yellow / caution)
    // ENHANCEMENT: Notify the UI when the buffer is full (red / warning)
    suspend fun read(): ByteArray {
        return withContext(Dispatchers.IO) {
            val available = dataLine.available()
            val readSize = if (available > minimumReadSize) available else minimumReadSize

            val chunk = ByteArray(readSize)
            dataLine.read(chunk, 0, readSize) // This will block until readSize bytes are available

            return@withContext chunk
        }
    }

    fun stop() {
        dataLine.stop()
        dataLine.close()
    }

}