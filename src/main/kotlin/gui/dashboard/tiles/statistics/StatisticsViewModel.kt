package gui.dashboard.tiles.statistics

import androidx.compose.runtime.MutableState

class StatisticsViewModel(
    val durationOfAudioUsed: MutableState<String>,
    val lowestDiscernibleFrequency: MutableState<String>,
    val frequencyResolution: MutableState<String>
)