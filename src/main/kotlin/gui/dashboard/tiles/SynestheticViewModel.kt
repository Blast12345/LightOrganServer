package gui.dashboard.tiles

import LightOrganStateMachine
import kotlinx.coroutines.flow.MutableStateFlow

// TODO: Is the view model named correctly?
class SynestheticViewModel(
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