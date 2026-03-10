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
        highPassFilter = null,
        lowPassFilter = null,
        sampleSize = sampleSize,
        interpolatedSampleSize = interpolatedSampleSize,
        magnitudeMultiplier = magnitudeMultiplier
    )
}
