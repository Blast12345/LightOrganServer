package gui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import config.ConfigSingleton
import gui.tiles.Stats.StatsViewModel
import gui.tiles.Stats.StatsViewModelFactory
import gui.tiles.Synesthetic.SynestheticViewModel
import sound.input.Input
import sound.input.finder.InputFinder


// TODO: This seems temporary
class DefaultInputFactory {

    fun create(): Input {
        val dataLine = InputFinder().getInput()
        return Input(dataLine)
    }

}

// TODO: Test me
data class ViewModel(
    val defaultInputFactory: DefaultInputFactory = DefaultInputFactory(),
    val input: MutableState<Input> = mutableStateOf(defaultInputFactory.create()),
    val statsViewModelFactory: StatsViewModelFactory = StatsViewModelFactory(),
    var synestheticViewModel: MutableState<SynestheticViewModel> = mutableStateOf(SynestheticViewModel(input = input.value)),
    var currentColor: MutableState<Color> = mutableStateOf(Color.Black),
    var statsViewModel: MutableState<StatsViewModel> = mutableStateOf(
        statsViewModelFactory.create(
            input.value,
            ConfigSingleton
        )
    )
) {

    private fun updateSynestheticTile() {
        // TODO: Factory
//        synestheticViewModel.value = createSynestheticViewModel()
    }

    private fun updateStatsTile() {
//        statsViewModel.value = statsViewModelFactory.create(input, ConfigSingleton)
    }

}

