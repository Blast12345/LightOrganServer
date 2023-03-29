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
    val newColor: (Color) -> Unit,
    private val input: Input,
    private val colorFactory: ColorFactoryInterface = ColorFactory(),
    private val server: ServerInterface = Server(),
    private val colorScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : InputDelegate {

    init {
        input.listeners.add(this)
    }

    override fun received(audio: AudioSignal) {
        colorScope.launch {
            val color = colorFactory.create(audio)
            broadcastColor(color)
        }
    }

    private fun broadcastColor(color: Color) {
        server.sendColor(color)
        newColor(color)
    }

    // TODO: Test me
    fun stop() {
        input.listeners.remove(this)
        // TODO: I should double check that UDP is significantly faster than TCP for my use case.
        // NOTE: There doesn't seem to be a reliable way to end on the color black. I can't promise delivery.
        // Maybe the client should show black if no color has been received for some duration?
    }

}

fun Color.toComposeColor(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color.hsv(
        hue = getHue() * 360,
        saturation = getSaturation(),
        value = getBrightness()
    )
}

fun Color.getHue(): Float {
    return Color.RGBtoHSB(red, green, blue, null)[0]
}

fun Color.getSaturation(): Float {
    return Color.RGBtoHSB(red, green, blue, null)[1]
}

fun Color.getBrightness(): Float {
    return Color.RGBtoHSB(red, green, blue, null)[2]
}