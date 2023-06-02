package config

import config.children.Client
import config.children.ColorWheel
import config.children.HighPassFilter
import config.children.MagnitudeEstimationStrategy
import kotlinx.coroutines.flow.MutableStateFlow

class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: Set<Client>,
    val colorWheel: ColorWheel,
    val highPassFilter: HighPassFilter,
    val sampleSize: Int,
    val interpolatedSampleSize: Int,
    val magnitudeEstimationStrategy: MagnitudeEstimationStrategy,
    val magnitudeMultiplier: Float,
    val millisecondsToWaitBetweenCheckingForNewAudio: Long,
    val noiseFloor: Float
)



