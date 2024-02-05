package toolkit.monkeyTest

import config.Config
import config.children.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

fun nextConfig(
    clients: Set<Client> = nextClients(),
    sampleSize: Int = nextPositiveInt(),
    interpolatedSampleSize: Int = nextPositiveInt(),
    magnitudeMultiplier: Float = Random.nextFloat()
): Config {
    return Config(
        startAutomatically = MutableStateFlow(Random.nextBoolean()),
        clients = clients,
        lowCrossover = nextCrossover(),
        highCrossover = nextCrossover(),
        sampleSize = sampleSize,
        decimationFactor = nextPositiveInt(),
        overlaps = nextPositiveInt(),
        overlapPercent = Random.nextFloat(),
        interpolatedSampleSize = interpolatedSampleSize,
        magnitudeMultiplier = magnitudeMultiplier,
        millisecondsToWaitBetweenCheckingForNewAudio = nextPositiveLong(),
    )
}
