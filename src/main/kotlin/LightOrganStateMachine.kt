import input.Input
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.LightOrgan
import lightOrgan.LightOrganSubscriber

class LightOrganStateMachine(
    private val input: Input,
    private val lightOrgan: LightOrgan
) {

    val isRunning: MutableStateFlow<Boolean> = MutableStateFlow(input.checkIfSubscribed(lightOrgan))

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