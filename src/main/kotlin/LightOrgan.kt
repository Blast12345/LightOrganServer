import color.ColorFactory
import color.ColorFactoryInterface
import server.Server
import server.ServerInterface
import sound.input.InputDelegate
import sound.input.InputInterface
import sound.input.samples.AudioSignal
import wrappers.SystemTime
import wrappers.SystemTimeInterface
import java.awt.Color

class LightOrgan(
    private val input: InputInterface,
    private val server: ServerInterface = Server(),
    private val colorFactory: ColorFactoryInterface = ColorFactory(),
    private val systemTime: SystemTimeInterface = SystemTime()
) : InputDelegate {

    private val maximumUpdatesPerSecond = 60
    private val minimumSecondsBetweenColors = 1.0 / maximumUpdatesPerSecond
    private var timestampOfLastSentColor = 0.0

    fun start() {
        // TODO: Listen on one thread and process colors on another
        input.listenForAudioSamples(this)
    }

    override fun receiveAudioSignal(audioSignal: AudioSignal) {
        if (shouldSendNextColor()) {
            sendColorToServer(audioSignal)
        }
    }

    private fun sendColorToServer(audioSignal: AudioSignal) {
        val color = getColor(audioSignal)
        server.sendColor(color)
        printLatency()
        updateTimestampOfLastSentColor()
    }

    private fun getColor(audioSignal: AudioSignal): Color {
        return colorFactory.create(audioSignal)
    }

    private fun printLatency() {
        val latencyInSeconds = secondsSinceLastSentColor()
        val latencyInMilliseconds = latencyInSeconds * 1000
        println("Latency: ${latencyInMilliseconds.toInt()}")
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