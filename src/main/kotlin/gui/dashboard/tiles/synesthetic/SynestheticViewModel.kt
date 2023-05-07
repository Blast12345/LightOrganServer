package gui.dashboard.tiles.synesthetic

import LightOrganStateMachine
import androidx.compose.runtime.MutableState
import config.Config
import config.PersistedConfig

class SynestheticViewModel(
    val startAutomatically: MutableState<Boolean>,
    val isRunning: MutableState<Boolean>,
    val lightOrganStateMachine: LightOrganStateMachine,
    private val config: Config = PersistedConfig()
) {

    fun toggleStartAutomatically() {
        val newValue = !startAutomatically.value
        startAutomatically.value = newValue
        config.startAutomatically = newValue
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