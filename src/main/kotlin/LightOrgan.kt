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

    fun start() {
        input.listenForAudioSamples(this)
    }

    override fun receiveAudioFrame(audioFrame: NormalizedAudioFrame) {
        sendColorToServer(audioFrame)
    }

    private fun sendColorToServer(audioFrame: NormalizedAudioFrame) {
        val color = getColor(audioFrame)
        server.sendColor(color)
    }

    private fun getColor(audioFrame: NormalizedAudioFrame): Color {
        val frequencyBins = getFrequencyBins(audioFrame)
        return colorFactory.create(frequencyBins)
    }

    private fun getFrequencyBins(audioFrame: NormalizedAudioFrame): FrequencyBins {
        return frequencyBinsFactory.create(audioFrame, lowestSupportedFrequency)
    }

}