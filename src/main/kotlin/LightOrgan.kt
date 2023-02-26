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

class LightOrgan(
    input: Input,
    private val colorFactory: ColorFactoryInterface = ColorFactory(),
    private val server: ServerInterface = Server(),
    private val colorScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : InputDelegate {

    init {
        input.setDelegate(this)
    }

    override fun received(audio: AudioSignal) {
        colorScope.launch {
            val color = colorFactory.create(audio)
            server.sendColor(color)
        }
    }

}

