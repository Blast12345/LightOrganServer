package lightOrgan

import gateway.GatewayManager
import input.DefaultInputFactory
import input.Input
import kotlinx.coroutines.flow.MutableStateFlow
import logging.Logger
import wrappers.color.Color


// TODO: I think this is the real light organ?
class LightOrganStateMachine(
    private val input: Input = DefaultInputFactory().create(),
    private val lightOrgan: LightOrgan = LightOrgan(),
    private val gatewayManager: GatewayManager
) : LightOrganSubscriber {

    val isRunning: MutableStateFlow<Boolean> = MutableStateFlow(lightOrganIsSubscribedToInput)

    private val lightOrganIsSubscribedToInput: Boolean
        get() = input.checkIfSubscribed(lightOrgan)

    fun start() {
        input.addSubscriber(lightOrgan)
        lightOrgan.addSubscriber(this) // TODO: Refactor
        isRunning.value = true
    }

    fun stop() {
        input.removeSubscriber(lightOrgan)
        lightOrgan.addSubscriber(this) // TODO: Refactor
        isRunning.value = false
    }

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        lightOrgan.addSubscriber(subscriber)
    }

    // TODO: Write a test that ensures that we don't crash the thread.
    override fun new(color: Color) {
        try {
            gatewayManager.currentGateway.value?.send(color)
        } catch (e: Exception) {
            Logger.error(e.message ?: "Failed to send color to gateway.")
        }
    }

}
