package audio.samples

class RollingAudioBuffer(
    private val rollingSampleBuffer: RollingSampleBuffer = RollingSampleBuffer()
) {

    private var format: AudioFormat? = null

    var size: Int
        get() = rollingSampleBuffer.size
        set(value) {
            rollingSampleBuffer.size = value
        }

    val current: AudioFrame?
        get() {
            val format = format ?: return null
            return AudioFrame(rollingSampleBuffer.current, format)
        }

    fun append(frame: AudioFrame): AudioFrame {
        if (frame.format != format) {
            format = frame.format
            rollingSampleBuffer.reset()
        }

        return AudioFrame(
            samples = rollingSampleBuffer.append(frame.samples),
            format = frame.format
        )
    }

}