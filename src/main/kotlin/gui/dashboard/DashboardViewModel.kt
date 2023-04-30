package gui.dashboard

import LightOrganStateMachine
import config.PersistedConfig
import gui.dashboard.tiles.color.ColorViewModel
import gui.dashboard.tiles.color.ColorViewModelFactory
import gui.dashboard.tiles.statistics.StatisticsViewModel
import gui.dashboard.tiles.statistics.StatisticsViewModelFactory
import gui.dashboard.tiles.synesthetic.SynestheticViewModel
import gui.dashboard.tiles.synesthetic.SynestheticViewModelFactory
import input.Input
import input.buffer.InputBuffer
import input.finder.InputFinder
import input.lineListener.LineListener
import server.Server

// TODO: This seems temporary
class DefaultInputFactory {

    fun create(): Input {
        val dataLine = InputFinder().getInput()
        val lineListener = LineListener(dataLine = dataLine)
        val buffer = InputBuffer(bufferSize = dataLine.bufferSize)
        return Input(
            lineListener = lineListener,
            buffer = buffer
        )
    }

}

// TODO: Test me
class DashboardViewModel(
    private val persistedConfig: PersistedConfig = PersistedConfig(),
    private val server: Server = Server(),
    private val lightOrganStateMachine: LightOrganStateMachine = LightOrganStateMachine(),
    val synestheticViewModel: SynestheticViewModel = SynestheticViewModelFactory().create(
        lightOrganStateMachine,
        persistedConfig
    ),
    val colorViewModel: ColorViewModel = ColorViewModelFactory().create(),
    val statisticsViewModel: StatisticsViewModel = StatisticsViewModelFactory().create(
        lightOrganStateMachine,
        persistedConfig
    )
) {

    init {
        addLightOrganSubscribers()
        startLightOrganIfNeeded()
    }

    private fun addLightOrganSubscribers() {
        lightOrganStateMachine.addSubscriber(colorViewModel)
        lightOrganStateMachine.addSubscriber(server)
    }

    private fun startLightOrganIfNeeded() {
        if (persistedConfig.startAutomatically) {
            synestheticViewModel.start()
        }
    }

}