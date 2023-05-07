package gui.dashboard

import LightOrganStateMachine
import gui.dashboard.tiles.SynestheticViewModel
import gui.dashboard.tiles.SynestheticViewModelFactory
import server.Server

class DashboardViewModel(
    private val server: Server = Server(),
    private val lightOrganStateMachine: LightOrganStateMachine = LightOrganStateMachine(),
    val synestheticViewModel: SynestheticViewModel = SynestheticViewModelFactory().create(lightOrganStateMachine)
) {

    init {
        addLightOrganSubscribers()
//        startLightOrganIfNeeded()
    }

    private fun addLightOrganSubscribers() {
        lightOrganStateMachine.addSubscriber(server)
    }
//
//    private fun startLightOrganIfNeeded() {
//        if (persistedConfig.startAutomatically) {
//            synestheticViewModel.start()
//        }
//    }

}