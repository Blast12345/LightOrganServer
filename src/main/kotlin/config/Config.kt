package config

import config.children.Client
import config.children.ColorWheel
import config.children.HighPassFilter
import config.children.MagnitudeEstimationStrategy
import kotlinx.coroutines.flow.MutableStateFlow

class Config(
    val startAutomatically: MutableStateFlow<Boolean>,
    val clients: List<Client> = listOf(Client("192.168.1.55")),
    val colorWheel: ColorWheel = ColorWheel(40F, 120F, 0.25F),
    val highPassFilter: HighPassFilter = HighPassFilter(120F, 15F),
    val sampleSize: Int = 4100,
    val interpolatedSampleSize: Int = 65536,
    val magnitudeEstimationStrategy: MagnitudeEstimationStrategy = MagnitudeEstimationStrategy(5),
    val magnitudeMultiplier: Float = 1.25F,
    val millisecondsToWaitBetweenCheckingForNewAudio: Long = 1,
    val noiseFloor: Float = 0.1F
)



