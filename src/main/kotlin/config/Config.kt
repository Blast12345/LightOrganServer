package config

import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import sound.frequencyBins.filters.Crossover
import sound.notes.Note

class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val bassLowCrossover: Crossover,
    val bassHighCrossover: Crossover,
    val rootNote: Note,
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val magnitudeMultiplier: Float,
    val millisecondsToWaitBetweenCheckingForNewAudio: Long
)



