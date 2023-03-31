import color.ColorFactory
import color.ColorFactoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import server.Server
import server.ServerInterface
import sound.input.Input
import sound.input.InputDelegate
import sound.input.samples.AudioSignal
import java.awt.Color

class LightOrgan(
    private val input: Input,
    private val colorFactory: ColorFactoryInterface = ColorFactory(),
    private val server: ServerInterface = Server(),
    private val colorScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : InputDelegate {

    val isRunning: Boolean
        get() = input.listeners.contains(this)

    fun start() {
        input.listeners.add(this)
    }

    fun stop() {
        input.listeners.remove(this)
    }

    override fun received(audio: AudioSignal) {
        colorScope.launch {
            val color = colorFactory.create(audio)
            broadcastColor(color)
        }
    }

    private fun broadcastColor(color: Color) {
        server.sendColor(color)
        // TODO: Pubsub color
    }

}
