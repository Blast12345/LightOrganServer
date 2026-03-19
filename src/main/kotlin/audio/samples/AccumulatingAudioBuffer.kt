package audio.samples

import extensions.clear
import kotlinx.coroutines.channels.Channel

// TODO: Test me
// Basic behavior
// Thread safety / locking?
class AccumulatingAudioBuffer {

    private var format: AudioFormat? = null
    private val cachedFrames = Channel<AudioFrame>(Channel.UNLIMITED)

    fun append(frame: AudioFrame) {
        if (format != frame.format) {
            cachedFrames.clear()
            format = frame.format
        }

        cachedFrames.trySend(frame)
    }

    // This will wait until the first frame becomes available
    suspend fun drain(): AudioFrame {
        val firstFrame = cachedFrames.receive()

        val frames = mutableListOf(firstFrame)

        while (true) {
            val nextFrame = cachedFrames.tryReceive().getOrNull() ?: break
            frames.add(nextFrame)
        }

        val format = firstFrame.format
        val samples = frames.fold(FloatArray(0)) { acc, frame -> acc + frame.samples }

        return AudioFrame(samples, format)
    }

}