package gui.dashboard.tiles

import ConfigSingleton
import LightOrganStateMachine
import config.Config

class LightOrganViewModelFactory(
    private val config: Config = ConfigSingleton
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): LightOrganViewModel {
        return LightOrganViewModel(
            startAutomatically = config.startAutomatically,
            isRunning = lightOrganStateMachine.isRunning,
            lightOrganStateMachine = lightOrganStateMachine
        )
    }

}