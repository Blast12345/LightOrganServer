package lightOrgan

import gateway.Gateway
import input.DefaultInputFactory
import input.Input
import kotlinx.coroutines.flow.MutableStateFlow

// TODO: I think this is the real light organ?
class LightOrganStateMachine(
    private val input: Input = DefaultInputFactory().create(),
    private val lightOrgan: LightOrgan = LightOrgan(),
    private val gateway: Gateway? = null
) {

    val isRunning: MutableStateFlow<Boolean> = MutableStateFlow(lightOrganIsSubscribedToInput)

    private val lightOrganIsSubscribedToInput: Boolean
        get() = input.checkIfSubscribed(lightOrgan)

    fun start() {
        input.addSubscriber(lightOrgan)
        isRunning.value = true
    }

    fun stop() {
        input.removeSubscriber(lightOrgan)
        isRunning.value = false
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        lightOrgan.addSubscriber(subscriber)
    }

}
