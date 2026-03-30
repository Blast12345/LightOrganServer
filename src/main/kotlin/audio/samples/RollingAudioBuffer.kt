package audio.samples

class RollingAudioBuffer(
    private val rollingSampleBuffer: RollingSampleBuffer = RollingSampleBuffer()
) {

    private var format: AudioFormat? = null

    fun append(frame: AudioFrame, requiredSize: Int): AudioFrame {
        if (frame.format != format) {
            format = frame.format
            rollingSampleBuffer.reset()
        }

        return AudioFrame(
            samples = rollingSampleBuffer.append(frame.samples, requiredSize),
            format = frame.format
        )
    }

}