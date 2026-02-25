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
    private val readSize: Int = 2048,
    private val bufferSize: Int = readSize * 4
) {

    val sampleRate = dataLine.format.sampleRate
    val bitDepth = dataLine.format.sampleSizeInBits
    val channels = dataLine.format.channels
    val byteOrder: ByteOrder = if (dataLine.format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN

    // If we want to modify read size, we should update the variable via a property on start
    // Also, we may want to expose samples per read and/or read interval
    fun start() {
        try {
            dataLine.open(dataLine.format, bufferSize)
            dataLine.start()
        } catch (e: Exception) {
            stop()
            throw e
        }
    }

    // TODO: Should we read all available data by default, else readSize?
    // TODO: Alternatively, should we notify when extra data was available OR if the buffer is full?
    suspend fun read(): ByteArray {
        return withContext(Dispatchers.IO) {
            val chunk = ByteArray(readSize)
            dataLine.read(chunk, 0, readSize)
            return@withContext chunk
        }
    }

    fun stop() {
        dataLine.stop()
        dataLine.close()
    }

}