package lightOrgan

import color.ColorFactory
import color.ColorFactoryInterface
import input.Input
import input.InputSubscriber
import input.audioFrame.AudioFrame
import java.awt.Color

class LightOrgan(
    input: Input,
    val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
    private val colorFactory: ColorFactoryInterface = ColorFactory()
) : InputSubscriber {

    init {
        input.subscribers.add(this)
    }

    override fun received(audioFrame: AudioFrame) {
        broadcast(
            color = colorFactory.create(audioFrame)
        )
    }

    private fun broadcast(color: Color) {
        subscribers.forEach {
            it.new(color)
        }
    }

}

