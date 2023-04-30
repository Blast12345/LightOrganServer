package gui.dashboard.tiles.synesthetic

import LightOrganStateMachine
import androidx.compose.runtime.MutableState
import config.PersistedConfig

// TODO: Test me
class SynestheticViewModel(
    val startAutomatically: MutableState<Boolean>,
    val isRunning: MutableState<Boolean>,
    private val lightOrganStateMachine: LightOrganStateMachine,
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun setStartAutomatically(value: Boolean) {
        persistedConfig.startAutomatically = value
        startAutomatically.value = value
    }

    fun start() {
        lightOrganStateMachine.start()
        updateRunningState() // TODO: Maybe there should be a way to observe the running state?
    }

    private fun updateRunningState() {
        isRunning.value = lightOrganStateMachine.isRunning
    }

    fun stop() {
        lightOrganStateMachine.stop()
        updateRunningState()
    }

}