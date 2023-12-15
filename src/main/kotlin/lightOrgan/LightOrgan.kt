package lightOrgan

import color.ColorFactory
import input.InputSubscriber
import input.audioFrame.AudioFrame
import wrappers.color.Color

class LightOrgan(
    private val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
    private val colorFactory: ColorFactory = ColorFactory(),
) : InputSubscriber {

    override fun received(audioFrame: AudioFrame) {
        broadcast(
            color = getColor(audioFrame)
        )
    }

    private fun broadcast(color: Color) {
        subscribers.forEach {
            it.new(color)
        }
    }

    private fun getColor(audioFrame: AudioFrame): Color {
        return colorFactory.create(
            audioFrame = audioFrame
        )
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}
