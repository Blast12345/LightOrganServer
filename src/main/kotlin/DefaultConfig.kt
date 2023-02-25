import config.*
import javax.sound.sampled.AudioFormat

class DefaultConfig(
    override val audioFormat: AudioFormat
) : Config {
    override val clients: List<Client> = listOf(Client("192.168.1.55"))
    override val colorWheel = ColorWheel(40F, 120F, 0.25F)
    override val highPassFilter = HighPassFilter(120F, 15F)
    override val sampleSize = 4100
    override val interpolatedSampleSize = 65536
    override val magnitudeEstimationStrategy = MagnitudeEstimationStrategy(5)
    override val magnitudeMultiplier: Float = 1.25F
    override val millisecondsToWaitBetweenCheckingForNewAudio: Long = 1
}