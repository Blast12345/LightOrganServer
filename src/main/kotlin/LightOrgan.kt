import color.ColorFactory
import color.ColorFactoryInterface
import config.ConfigSingleton
import input.Input
import input.InputSubscriber
import input.audioFrame.AudioFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import server.Server
import server.ServerInterface

class LightOrgan(
    input: Input,
    private val colorFactory: ColorFactoryInterface = ColorFactory(),
    private val server: ServerInterface = Server(ConfigSingleton.clients),
    private val colorScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : InputSubscriber {

    init {
        input.subscribers.add(this)
    }

    override fun received(audioFrame: AudioFrame) {
        colorScope.launch {
            val color = colorFactory.create(audioFrame)
            server.sendColor(color)
        }
    }

}

