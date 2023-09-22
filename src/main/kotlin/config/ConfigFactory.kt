package config

import config.children.Client
import config.children.ColorWheel
import config.children.MagnitudeEstimationStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import sound.frequencyBins.filters.PassFilter
import sound.notes.Notes

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            colorWheel = ColorWheel(40F, 120F, 0.25F),
            highPassFilter = PassFilter(Notes.C.getFrequency(0), Notes.C.getFrequency(1)),
            lowPassFilter = PassFilter(Notes.C.getFrequency(2), Notes.C.getFrequency(3)),
            sampleSize = 4100,
            interpolatedSampleSize = 65536,
            magnitudeEstimationStrategy = MagnitudeEstimationStrategy(5),
            magnitudeMultiplier = 2F,
            millisecondsToWaitBetweenCheckingForNewAudio = 1,
            noiseFloor = 0.1F
        )
    }

}