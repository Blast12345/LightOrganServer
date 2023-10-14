package config

import config.children.Client
import config.children.ColorWheel
import config.children.MagnitudeEstimationStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import sound.frequencyBins.filters.Crossover
import sound.notes.Notes

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            colorWheel = ColorWheel(
                startingFrequency = 40F,
                endingFrequency = 120F,
                offset = 0.25F
            ),
            bassLowCrossover = Crossover(
                stopFrequency = Notes.C.getFrequency(0),
                cornerFrequency = Notes.C.getFrequency(1)
            ),
            bassHighCrossover = Crossover(
                cornerFrequency = Notes.C.getFrequency(2),
                stopFrequency = Notes.C.getFrequency(3)
            ),
            sampleSize = 4100,
            interpolatedSampleSize = 65536,
            magnitudeEstimationStrategy = MagnitudeEstimationStrategy(5),
            magnitudeMultiplier = 2F,
            millisecondsToWaitBetweenCheckingForNewAudio = 1,
            noiseFloor = 0.1F
        )
    }

}