package toolkit.monkeyTest

import config.Config
import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

fun nextConfig(clients: Set<Client> = nextClients()): Config {
    return Config(
        startAutomatically = MutableStateFlow(Random.nextBoolean()),
        clients = clients,
        colorWheel = nextColorWheel(),
        highPassFilter = nextHighPassFilter(),
        sampleSize = nextPositiveInt(),
        interpolatedSampleSize = nextPositiveInt(),
        magnitudeEstimationStrategy = nextMagnitudeEstimationStrategy(),
        magnitudeMultiplier = Random.nextFloat(),
        millisecondsToWaitBetweenCheckingForNewAudio = nextPositiveLong(),
        noiseFloor = Random.nextFloat()
    )
}