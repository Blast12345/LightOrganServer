package config

import config.children.Client
import config.children.ColorWheel
import config.children.MagnitudeEstimationStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import sound.frequencyBins.filters.PassFilter

class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val colorWheel: ColorWheel,
    val lowPassFilter: PassFilter,
    val highPassFilter: PassFilter,
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val magnitudeEstimationStrategy: MagnitudeEstimationStrategy,
    val magnitudeMultiplier: Float,
    val millisecondsToWaitBetweenCheckingForNewAudio: Long,
    val noiseFloor: Float
)



