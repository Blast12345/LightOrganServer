package audio.samples

// TODO: Test
class RollingAudioBuffer(
    private val capacity: Int
) {

    private var format: AudioFormat? = null
    private val rollingSampleBuffer: RollingSampleBuffer = RollingSampleBuffer(capacity)

    val current: AudioFrame?
        get() = format?.let { AudioFrame(rollingSampleBuffer.current, it) }

    fun append(frame: AudioFrame) {
        if (frame.format != format) {
            rollingSampleBuffer.reset()
        }

        format = frame.format
        rollingSampleBuffer.append(frame.samples)
    }

    fun reset() {
        rollingSampleBuffer.reset()
    }

}