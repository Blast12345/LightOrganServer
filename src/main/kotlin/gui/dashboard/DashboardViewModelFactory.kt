package gui.dashboard

import LightOrganStateMachine
import gui.dashboard.tiles.SynestheticViewModelFactory

class DashboardViewModelFactory(
    private val synestheticViewModelFactory: SynestheticViewModelFactory = SynestheticViewModelFactory()
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine): DashboardViewModel {
        return DashboardViewModel(
            synestheticViewModel = synestheticViewModelFactory.create(lightOrganStateMachine)
        )
    }

}