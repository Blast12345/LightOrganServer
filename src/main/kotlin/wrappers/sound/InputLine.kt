package wrappers.sound

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
    private val bufferSize: Int = 8192, // ENHANCEMENT: Make the buffer size configurable in the GUI.
) {

    val sampleRate = dataLine.format.sampleRate
    val bitDepth = dataLine.format.sampleSizeInBits
    val channels = dataLine.format.channels
    val byteOrder: ByteOrder = if (dataLine.format.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN

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
    @Suppress("RedundantSuspendModifier")
    suspend fun read(): ReadResult {
        val frameSize = dataLine.format.frameSize
        require(frameSize > 0) { "Cannot read: audio format has no valid frame size ($frameSize)" }

        val firstFrame = ByteArray(frameSize)
        dataLine.read(firstFrame, 0, frameSize)

        val remaining = dataLine.available()
        val remainingFrames = ByteArray(remaining)
        dataLine.read(remainingFrames, 0, remaining)

        return ReadResult(firstFrame + remainingFrames, (frameSize + remaining) >= bufferSize)
    }

    class ReadResult(
        val data: ByteArray,
        val bufferWasFull: Boolean
    )

}