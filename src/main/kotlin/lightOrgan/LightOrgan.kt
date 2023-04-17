package lightOrgan

import color.ColorFactory
import color.ColorFactoryInterface
import input.Input
import input.InputSubscriber
import input.audioFrame.AudioFrame
import java.awt.Color

interface LightOrganListener {
    fun new(color: Color)
}

class LightOrgan(
    private val input: Input,
    private val colorFactory: ColorFactoryInterface = ColorFactory()
) : InputSubscriber {

    val listeners: MutableSet<LightOrganListener> = mutableSetOf()

    fun startListeningToInput() {
        input.subscribers.add(this)
    }

    override fun received(audio: AudioFrame) {
        val color = colorFactory.create(audio)
        broadcast(color)
    }

    private fun broadcast(color: Color) {
        listeners.forEach {
            it.new(color)
        }
    }

    fun stopListeningToInput() {
        input.subscribers.remove(this)
    }

}
