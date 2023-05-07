package lightOrgan

import color.ColorFactory
import color.ColorFactoryInterface
import input.InputSubscriber
import input.audioFrame.AudioFrame
import java.awt.Color

class LightOrgan(
    val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
    private val colorFactory: ColorFactoryInterface = ColorFactory()
) : InputSubscriber {

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

