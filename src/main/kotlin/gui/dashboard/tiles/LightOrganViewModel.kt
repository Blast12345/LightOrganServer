package gui.dashboard.tiles

import LightOrganStateMachine
import kotlinx.coroutines.flow.MutableStateFlow

class LightOrganViewModel(
    val startAutomatically: MutableStateFlow<Boolean>,
    val isRunning: MutableStateFlow<Boolean>,
    val lightOrganStateMachine: LightOrganStateMachine
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

}