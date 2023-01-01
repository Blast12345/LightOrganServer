import color.ColorFactory
import color.ColorFactoryInterface
import server.Server
import server.ServerInterface
import sound.input.InputInterface
import sound.input.sample.AudioFrame

class LightOrgan(
    private var input: InputInterface,
    private var colorFactory: ColorFactoryInterface = ColorFactory(),
    private var server: ServerInterface = Server()
) {

    fun start() {
        input.listenForAudioSamples { nextAudioFrame ->
            sendColorFor(nextAudioFrame)
        }
    }

    private fun sendColorFor(audioFrame: AudioFrame) {
        // TODO: Sleep if we are sending colors too quickly?
        val color = colorFactory.colorFor(audioFrame)
        server.sendColor(color)
    }

}