package config

import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import sound.bins.frequency.filters.Crossover
import sound.notes.Notes

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            lowCrossover = Crossover(
                stopFrequency = Notes.C.getFrequency(1),
                cornerFrequency = Notes.C.getFrequency(2)
            ),
            highCrossover = Crossover(
                cornerFrequency = Notes.C.getFrequency(2),
                stopFrequency = Notes.C.getFrequency(3)
            ),
            sampleSize = 4410,
            interpolatedSampleSize = 65536,
            magnitudeMultiplier = 2F,
            millisecondsToWaitBetweenCheckingForNewAudio = 1,
        )
    }

}