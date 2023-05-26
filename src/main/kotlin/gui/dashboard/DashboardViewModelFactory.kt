package gui.dashboard

import LightOrganStateMachine
import gui.dashboard.tiles.LightOrganViewModelFactory

class DashboardViewModelFactory(
    private val lightOrganViewModelFactory: LightOrganViewModelFactory = LightOrganViewModelFactory()
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): DashboardViewModel {
        return DashboardViewModel(
            lightOrganViewModel = lightOrganViewModelFactory.create(lightOrganStateMachine)
        )
    }

}