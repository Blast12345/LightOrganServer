package gui.dashboard.tiles.lightOrgan

import ConfigSingleton
import config.Config
import lightOrgan.LightOrganStateMachine

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