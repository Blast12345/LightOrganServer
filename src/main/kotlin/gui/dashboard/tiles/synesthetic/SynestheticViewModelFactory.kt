package gui.dashboard.tiles.synesthetic

import LightOrganStateMachine
import androidx.compose.runtime.mutableStateOf
import config.Config

class SynestheticViewModelFactory {

    fun create(lightOrganStateMachine: LightOrganStateMachine, config: Config): SynestheticViewModel {
        return SynestheticViewModel(
            startAutomatically = mutableStateOf(config.startAutomatically),
            isRunning = mutableStateOf(lightOrganStateMachine.isRunning),
            lightOrganStateMachine = lightOrganStateMachine
        )
    }

}