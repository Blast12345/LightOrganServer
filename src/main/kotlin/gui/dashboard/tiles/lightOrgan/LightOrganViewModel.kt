package gui.dashboard.tiles.lightOrgan

import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.LightOrganStateMachine
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

    fun addSubscriber(subscriber: LightOrganSubscriber) {
        lightOrganStateMachine.addSubscriber(subscriber)
    }

}