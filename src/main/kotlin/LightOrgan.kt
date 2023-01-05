import color.ColorFactory
import color.ColorFactoryInterface
import server.Server
import server.ServerInterface
import sound.frequencyBins.FrequencyBins
import sound.frequencyBins.FrequencyBinsFactory
import sound.frequencyBins.FrequencyBinsFactoryInterface
import sound.input.InputDelegate
import sound.input.InputInterface
import sound.input.samples.AudioFrame
import wrappers.SystemTime
import wrappers.SystemTimeInterface
import java.awt.Color

class LightOrgan(
    private val input: InputInterface,
    private val server: ServerInterface = Server(),
    private val colorFactory: ColorFactoryInterface = ColorFactory(),
    private val frequencyBinsFactory: FrequencyBinsFactoryInterface = FrequencyBinsFactory(),
    private val systemTime: SystemTimeInterface = SystemTime()
) : InputDelegate {

    private val lowestSupportedFrequency = 20F
    private val maximumUpdatesPerSecond = 60
    private val minimumSecondsBetweenColors = 1.0 / maximumUpdatesPerSecond
    private var timestampOfLastSentColor = 0.0

    fun start() {
        input.listenForAudioSamples(this)
    }

    override fun receiveAudioFrame(audioFrame: AudioFrame) {
        if (shouldSendNextColor()) {
            sendColorToServer(audioFrame)
        }
    }

    private fun sendColorToServer(audioFrame: AudioFrame) {
        val color = getColor(audioFrame)
        server.sendColor(color)
        updateTimestampOfLastSentColor()
    }

    private fun getColor(audioFrame: AudioFrame): Color {
        val frequencyBins = getFrequencyBins(audioFrame)
        return colorFactory.create(frequencyBins)
    }

    private fun getFrequencyBins(audioFrame: AudioFrame): FrequencyBins {
        return frequencyBinsFactory.create(audioFrame, lowestSupportedFrequency)
    }

    private fun updateTimestampOfLastSentColor() {
        timestampOfLastSentColor = systemTime.currentTimeInSeconds()
    }

    private fun shouldSendNextColor(): Boolean {
        return minimumSecondsBetweenColors <= secondsSinceLastSentColor()
    }

    private fun secondsSinceLastSentColor(): Double {
        return systemTime.currentTimeInSeconds() - timestampOfLastSentColor
    }

}