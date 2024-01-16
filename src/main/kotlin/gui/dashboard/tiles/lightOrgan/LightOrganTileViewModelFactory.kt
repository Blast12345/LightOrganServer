package gui.dashboard.tiles.lightOrgan

import config.Config
import config.ConfigSingleton
import lightOrgan.LightOrganStateMachine

class LightOrganTileViewModelFactory(
    private val config: Config = ConfigSingleton
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): LightOrganTileViewModel {
        return LightOrganTileViewModel(
            startAutomatically = config.startAutomatically,
            isRunning = lightOrganStateMachine.isRunning,
            lightOrganStateMachine = lightOrganStateMachine
        )
    }

}
