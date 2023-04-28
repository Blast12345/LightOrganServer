package gui.dashboard

import androidx.compose.runtime.mutableStateOf
import config.ConfigSingleton
import config.ConfigStorage
import gui.dashboard.tiles.color.ColorViewModel
import gui.dashboard.tiles.color.ColorViewModelFactory
import gui.dashboard.tiles.statistics.StatisticsViewModel
import gui.dashboard.tiles.statistics.StatisticsViewModelFactory
import input.Input
import input.buffer.InputBuffer
import input.finder.InputFinder
import input.lineListener.LineListener
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lightOrgan.LightOrgan
import lightOrgan.LightOrganListener
import server.Server
import java.awt.Color

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
    private val colorViewModelFactory: ColorViewModelFactory = ColorViewModelFactory(),
    private val statisticsViewModelFactory: StatisticsViewModelFactory = StatisticsViewModelFactory()
) : LightOrganListener {

    private var input = DefaultInputFactory().create()
    private var lightOrgan = LightOrgan(input)
    private var server = Server()
    private var config = ConfigSingleton

    val startAutomatically = mutableStateOf(ConfigSingleton.startAutomatically)
    val isRunning = mutableStateOf(false)
    val colorViewModelState = mutableStateOf(ColorViewModel())
    val statisticsViewModelState = mutableStateOf(StatisticsViewModel())

    init {
        startLightOrganIfNeeded()
        subscribeToLightOrgan()
        updateStatsTile()
    }

    private fun startLightOrganIfNeeded() {
        if (ConfigStorage().get()?.startAutomatically == true) {
            startPressed()
        }
    }

    private fun subscribeToLightOrgan() {
        lightOrgan.listeners.add(this)
    }

    fun startAutomaticallyPressed(value: Boolean) {
        ConfigSingleton.startAutomatically = value
        ConfigStorage().set(ConfigSingleton)
        startAutomatically.value = value
    }

    fun startPressed() {
        lightOrgan.startListeningToInput()
        isRunning.value = true
    }

    fun stopPressed() {
        lightOrgan.stopListeningToInput()
        isRunning.value = false
    }

    private fun updateColorTile(color: Color = Color.BLACK) {
        colorViewModelState.value = colorViewModelFactory.create(color)
    }

    private fun updateStatsTile() {
        statisticsViewModelState.value = statisticsViewModelFactory.create(input.audioFormat, config)
    }

    // Color Delegate
    override fun new(color: Color) {
        server.sendColor(color)

        MainScope().launch {
            this@DashboardViewModel.updateColorTile(
                color = color
            )
        }
    }

}


