package gui.dashboard.tiles

import LightOrganStateMachine
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow

class SynestheticViewModel(
    val startAutomatically: MutableStateFlow<Boolean>,
    val isRunning: MutableState<Boolean>,
    val lightOrganStateMachine: LightOrganStateMachine
) {

    fun setStartAutomatically(newValue: Boolean) {
        startAutomatically.value = newValue
    }

    fun start() {
        lightOrganStateMachine.start()
        isRunning.value = true
    }

    fun stop() {
        lightOrganStateMachine.stop()
        isRunning.value = false
    }

}