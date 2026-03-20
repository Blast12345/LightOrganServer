package audio.samples

class RollingAudioBuffer(
    private val capacity: Int
) {

    private var format: AudioFormat? = null
    private val rollingSampleBuffer: RollingSampleBuffer = RollingSampleBuffer(capacity)

    fun append(frame: AudioFrame): AudioFrame {
        if (frame.format != format) {
            reset()
        }

        format = frame.format
        rollingSampleBuffer.append(frame.samples)

        return AudioFrame(rollingSampleBuffer.current, frame.format)
    }

    private fun reset() {
        format = null
        rollingSampleBuffer.reset()
    }

}