import colorListener.ColorListener
import colorListener.ColorListenerInterface
import server.Server
import server.ServerInterface
import sound.input.InputInterface

class LightOrgan(
    private var input: InputInterface,
    private var colorFactory: ColorListenerInterface = ColorListener(),
    private var server: ServerInterface = Server()
) {

    fun start() {
        input.listenForAudioSamples { nextSample ->
            sendColorFor(nextSample)
        }
    }

    private fun sendColorFor(sample: DoubleArray) {
        // TODO: Sleep if we are sending colors too quickly?
        val color = colorFactory.colorFor(sample)
        server.sendColor(color)
    }

}