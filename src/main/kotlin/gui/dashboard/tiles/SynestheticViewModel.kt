package gui.dashboard.tiles

import LightOrganStateMachine
import kotlinx.coroutines.flow.MutableStateFlow

class SynestheticViewModel(
    val startAutomatically: MutableStateFlow<Boolean>,
    val isRunning: MutableStateFlow<Boolean>,
    val lightOrganStateMachine: LightOrganStateMachine
) {

    fun start() {
        lightOrganStateMachine.start()
    }

    fun stop() {
        lightOrganStateMachine.stop()
    }

}