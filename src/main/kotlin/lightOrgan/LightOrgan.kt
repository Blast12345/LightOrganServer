package lightOrgan

import input.Input
import input.InputDelegate
import input.samples.AudioSignal
import lightOrgan.color.ColorFactory
import lightOrgan.color.ColorFactoryInterface
import java.awt.Color

interface LightOrganListener {
    fun new(color: Color)
}

class LightOrgan(
    private val input: Input,
    private val colorFactory: ColorFactoryInterface = ColorFactory()
) : InputDelegate {

    val listeners: MutableSet<LightOrganListener> = mutableSetOf()

    fun startListeningToInput() {
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

    fun stopListeningToInput() {
        input.listeners.remove(this)
    }

}
