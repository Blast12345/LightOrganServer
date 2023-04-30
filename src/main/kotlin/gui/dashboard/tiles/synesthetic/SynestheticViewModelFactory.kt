package gui.dashboard.tiles.synesthetic

import LightOrganStateMachine
import androidx.compose.runtime.mutableStateOf
import config.PersistedConfig

// TODO: Test me
class SynestheticViewModelFactory {

    fun create(lightOrganStateMachine: LightOrganStateMachine, persistedConfig: PersistedConfig): SynestheticViewModel {
        return SynestheticViewModel(
            startAutomatically = mutableStateOf(persistedConfig.startAutomatically),
            isRunning = mutableStateOf(lightOrganStateMachine.isRunning),
            lightOrganStateMachine = lightOrganStateMachine
        )
    }

}