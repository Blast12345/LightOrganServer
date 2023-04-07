package lightOrgan

import lightOrgan.color.ColorFactory
import lightOrgan.color.ColorFactoryInterface
import lightOrgan.sound.input.Input
import lightOrgan.sound.input.InputDelegate
import lightOrgan.sound.input.samples.AudioSignal
import java.awt.Color

interface LightOrganListener {
    fun new(color: Color)
}

class LightOrgan(
    private val input: Input,
    val listeners: MutableSet<LightOrganListener> = mutableSetOf(),
    private val colorFactory: ColorFactoryInterface = ColorFactory()
) : InputDelegate {

    fun start() {
        input.listeners.add(this)
    }

    override fun received(audio: AudioSignal) {
        val color = colorFactory.create(audio)
        broadcast(color)
    }

    private fun broadcast(color: Color) {
        listeners.forEach {
            it.new(color)
        }
    }

    fun stop() {
        input.listeners.remove(this)
    }

}
