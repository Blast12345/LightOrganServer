package gui.dashboard.tiles

import LightOrganStateMachine
import androidx.compose.runtime.mutableStateOf
import config.Config
import config.ConfigProvider

class SynestheticViewModelFactory(
    private val config: Config = ConfigProvider().current
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): SynestheticViewModel {
        return SynestheticViewModel(
            startAutomatically = config.startAutomatically,
            isRunning = mutableStateOf(lightOrganStateMachine.isRunning),
            lightOrganStateMachine = lightOrganStateMachine
        )
    }

}