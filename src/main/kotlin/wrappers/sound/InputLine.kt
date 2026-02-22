package wrappers.sound

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.ByteOrder
import javax.sound.sampled.TargetDataLine

class InputLine(
    val name: String,
    private val dataLine: TargetDataLine,
    private val readSize: Int = 2048
) {

    val sampleRate = dataLine.format.sampleRate.toInt()
    val bitDepth = dataLine.format.sampleSizeInBits
    val channels = dataLine.format.channels
    val byteOrder: ByteOrder = if (dataLine.format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN

    // If we want to modify read size, we should update the variable via a property on start
    // Also, we may want to expose samples per read and/or read interval
    fun start() {
        try {
            dataLine.open(dataLine.format, readSize)
            dataLine.start()
        } catch (e: Exception) {
            stop()
            throw e
        }
    }

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