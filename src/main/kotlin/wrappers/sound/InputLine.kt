package wrappers.sound

import annotations.Wrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.ByteOrder
import javax.sound.sampled.TargetDataLine
import kotlin.time.Duration.Companion.seconds

// TODO: Start adding tests?
@Wrapper
class InputLine(
    val name: String,
    private val dataLine: TargetDataLine,
    private val chunkSize: Int = 2048, // TODO: Make this configurable.
) {

    val sampleRate = dataLine.format.sampleRate.toInt()
    val bitDepth = dataLine.format.sampleSizeInBits
    val channels = dataLine.format.channels
    val byteOrder: ByteOrder = if (dataLine.format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN

    val samplesPerChunk = chunkSize / (channels * (bitDepth / 8))
    val captureInterval = (samplesPerChunk.toDouble() / sampleRate).seconds

    fun start() {
        try {
            dataLine.open(dataLine.format, chunkSize)
            dataLine.start()
        } catch (e: Exception) {
            stop()
            throw e
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