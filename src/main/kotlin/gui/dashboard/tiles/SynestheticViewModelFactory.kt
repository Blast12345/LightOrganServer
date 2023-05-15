package gui.dashboard.tiles

import ConfigSingleton
import LightOrganStateMachine
import config.Config

class SynestheticViewModelFactory(
    private val config: Config = ConfigSingleton
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): SynestheticViewModel {
        return SynestheticViewModel(
            startAutomatically = config.startAutomatically,
            isRunning = lightOrganStateMachine.isRunning,
            lightOrganStateMachine = lightOrganStateMachine
        )
    }

}