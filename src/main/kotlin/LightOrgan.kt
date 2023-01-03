import color.ColorFactory
import color.ColorFactoryInterface
import server.Server
import server.ServerInterface
import sound.frequencyBins.FrequencyBins
import sound.frequencyBins.FrequencyBinsFactory
import sound.frequencyBins.FrequencyBinsFactoryInterface
import sound.input.InputDelegate
import sound.input.InputInterface
import sound.input.samples.NormalizedAudioFrame
import java.awt.Color

class LightOrgan(
    private val input: InputInterface,
    private val server: ServerInterface = Server(),
    private val colorFactory: ColorFactoryInterface = ColorFactory(),
    private val frequencyBinsFactory: FrequencyBinsFactoryInterface = FrequencyBinsFactory()
) : InputDelegate {

    private val lowestSupportedFrequency = 20F

    suspend fun start() {
        input.listenForAudioSamples(this)
    }

    override fun receiveAudioFrame(audioFrame: NormalizedAudioFrame) {
        // TODO: Sleep if we are sending colors too quickly?
        sendColorToServerFor(audioFrame)
    }

    private fun sendColorToServerFor(audioFrame: NormalizedAudioFrame) {
        val color = getColorFor(audioFrame)
        server.sendColor(color)
    }

    private fun getColorFor(audioFrame: NormalizedAudioFrame): Color {
        val frequencyBins = getFrequencyBinsFor(audioFrame)
        return colorFactory.createFor(frequencyBins)
    }

    private fun getFrequencyBinsFor(audioFrame: NormalizedAudioFrame): FrequencyBins {
        return frequencyBinsFactory.createFrom(audioFrame, lowestSupportedFrequency)
    }

}