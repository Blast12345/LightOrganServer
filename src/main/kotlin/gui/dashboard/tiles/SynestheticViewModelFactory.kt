package gui.dashboard.tiles

import LightOrganStateMachine
import config.Config
import config.ConfigProvider

class SynestheticViewModelFactory(
    private val config: Config = ConfigProvider().current
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): SynestheticViewModel {
        return SynestheticViewModel(
            startAutomatically = config.startAutomatically,
            isRunning = lightOrganStateMachine.isRunning,
            lightOrganStateMachine = lightOrganStateMachine
        )
    }

}