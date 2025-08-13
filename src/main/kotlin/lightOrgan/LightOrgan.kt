package lightOrgan

import color.ColorFactory
import input.InputSubscriber
import input.audioFrame.AudioFrame
import wrappers.color.Color

// TODO: This layer feels superfluous
class LightOrgan(
    private val subscribers: MutableSet<LightOrganSubscriber> = mutableSetOf(),
    private val colorFactory: ColorFactory = ColorFactory(),
) : InputSubscriber {

    private var timestampOfLastSentColor = System.currentTimeMillis()

    override fun received(audioFrame: AudioFrame) {
        broadcast(
            color = getColor(audioFrame)
        )

        printLatency()
    }

    private fun getColor(audioFrame: AudioFrame): Color {
        return colorFactory.create(
            audioFrame = audioFrame
        )
    }

    private fun broadcast(color: Color) {
        subscribers.forEach {
            it.new(color)
        }
    }

    private fun printLatency() {
        val timeBetweenColors = System.currentTimeMillis() - timestampOfLastSentColor
        println("Latency: $timeBetweenColors")
        timestampOfLastSentColor = System.currentTimeMillis()
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        subscribers.add(subscriber)
    }

}
