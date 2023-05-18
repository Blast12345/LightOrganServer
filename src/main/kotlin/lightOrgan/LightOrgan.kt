package lightOrgan

import color.ColorFactory
import input.Input
import input.InputSubscriber
import input.audioFrame.AudioFrame
import java.awt.Color

class LightOrgan(
    input: Input,
    private val colorFactory: ColorFactory = ColorFactory(),
    private val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf()
) : InputSubscriber {

    init {
        input.addSubscriber(this)
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

    fun checkIfSubscribed(subscriber: LightOrganSubscriber): Boolean {
        return subscribers.contains(subscriber)
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}

