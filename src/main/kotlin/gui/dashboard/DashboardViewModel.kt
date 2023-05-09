package gui.dashboard

import LightOrganStateMachine
import gui.dashboard.tiles.SynestheticViewModel
import gui.dashboard.tiles.SynestheticViewModelFactory
import server.Server

// TODO: Factory?
class DashboardViewModel(
    private val lightOrganStateMachine: LightOrganStateMachine = LightOrganStateMachine(),
    private val server: Server = Server(),
    val synestheticViewModel: SynestheticViewModel = SynestheticViewModelFactory().create(lightOrganStateMachine)
) {

    init {
        addLightOrganSubscribers()
        startLightOrganIfNeeded()
    }

    private fun addLightOrganSubscribers() {
        lightOrganStateMachine.addSubscriber(server)
    }

    private fun startLightOrganIfNeeded() {
        if (synestheticViewModel.startAutomatically.value) {
            synestheticViewModel.start()
        }
    }

}