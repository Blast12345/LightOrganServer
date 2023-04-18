package toolkit.monkeyTest

import config.Config
import kotlin.random.Random

fun nextConfig(): Config {
    return Config(
        startAutomatically = Random.nextBoolean(),
        sampleSize = nextPositiveInt(),
        interpolatedSampleSize = nextPositiveInt(),
        magnitudeMultiplier = Random.nextFloat(),
        millisecondsToWaitBetweenCheckingForNewAudio = nextPositiveLong()
    )
}