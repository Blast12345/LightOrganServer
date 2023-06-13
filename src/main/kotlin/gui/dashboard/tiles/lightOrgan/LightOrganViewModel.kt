package gui.dashboard.tiles.lightOrgan

import LightOrganStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.LightOrganSubscriber

class LightOrganViewModel(
    val startAutomatically: MutableStateFlow<Boolean>,
    val isRunning: MutableStateFlow<Boolean>,
    private val lightOrganStateMachine: LightOrganStateMachine
) {

    init {
        startLightOrganIfNeeded()
    }

    private fun startLightOrganIfNeeded() {
        if (startAutomatically.value) {
            start()
        }
    }

    fun start() {
        lightOrganStateMachine.start()
    }

    fun stop() {
        lightOrganStateMachine.stop()
    }

    // TODO: Test me
    fun addSubscriber(subscriber: LightOrganSubscriber) {
        lightOrganStateMachine.addSubscriber(subscriber)
    }

}