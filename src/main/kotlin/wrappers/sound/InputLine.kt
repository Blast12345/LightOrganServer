package wrappers.sound

import annotations.Wrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.nio.ByteOrder
import javax.sound.sampled.TargetDataLine

// TODO: Start adding tests?
@Wrapper
class InputLine(
    val name: String,
    private val dataLine: TargetDataLine,
) {

    val sampleRate = dataLine.format.sampleRate.toInt()
    val bitDepth = dataLine.format.sampleSizeInBits
    val channels = dataLine.format.channels
    val byteOrder: ByteOrder = if (dataLine.format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN
    private var chunkSize: Int = 0

    suspend fun start(timeout: Long = 1000) {
        try {
            dataLine.open()
            dataLine.start()
            withTimeout(timeout) { updateChunkSize() }
        } catch (e: Exception) {
            stop()
            throw e
        }
    }

    private fun updateChunkSize() {
        while (chunkSize == 0) {
            chunkSize = dataLine.available()
        }
    }

    suspend fun read(): ByteArray {
        return withContext(Dispatchers.IO) {
            val chunk = ByteArray(chunkSize)
            dataLine.read(chunk, 0, chunkSize)
            return@withContext chunk
        }
    }

    fun stop() {
        dataLine.stop()
        dataLine.close()
    }

}