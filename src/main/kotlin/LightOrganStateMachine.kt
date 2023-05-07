import input.Input
import lightOrgan.LightOrgan
import lightOrgan.LightOrganSubscriber

class LightOrganStateMachine(
    val input: Input = DefaultInputFactory().create(),
    private val lightOrgan: LightOrgan = LightOrgan(),
) {

    val isRunning: Boolean
        get() = input.subscribers.contains(lightOrgan)

    fun start() {
        input.subscribers.add(lightOrgan)
    }

    fun stop() {
        input.subscribers.remove(lightOrgan)
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        lightOrgan.subscribers.add(subscriber)
    }

}