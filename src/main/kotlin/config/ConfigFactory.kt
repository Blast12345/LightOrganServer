package config

import config.children.Client
import config.children.ColorWheel
import config.children.HighPassFilter
import config.children.MagnitudeEstimationStrategy
import kotlinx.coroutines.flow.MutableStateFlow

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {
    
    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            colorWheel = ColorWheel(40F, 120F, 0.25F),
            highPassFilter = HighPassFilter(120F, 15F),
            sampleSize = 4100,
            interpolatedSampleSize = 65536,
            magnitudeEstimationStrategy = MagnitudeEstimationStrategy(5),
            magnitudeMultiplier = 1.25F,
            millisecondsToWaitBetweenCheckingForNewAudio = 1,
            noiseFloor = 0.1F
        )
    }

}