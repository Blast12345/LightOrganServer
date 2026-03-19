package audio.samples

class RollingAudioBuffer(
    private val capacity: Int
) {

    private var format: AudioFormat? = null
    private val rollingSampleBuffer: RollingSampleBuffer = RollingSampleBuffer(capacity)

    val current: AudioFrame?
        get() = format?.let { AudioFrame(rollingSampleBuffer.current, it) }

    fun append(frame: AudioFrame) {
        if (frame.format != format) {
            reset()
        }

        format = frame.format
        rollingSampleBuffer.append(frame.samples)
    }

    private fun reset() {
        format = null
        rollingSampleBuffer.reset()
    }

}