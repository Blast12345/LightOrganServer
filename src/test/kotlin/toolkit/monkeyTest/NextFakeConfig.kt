package toolkit.monkeyTest

import config.FakeConfig
import kotlin.random.Random

fun nextFakeConfig(): FakeConfig {
    return FakeConfig(
        startAutomatically = Random.nextBoolean(),
        clients = nextClientList(),
        colorWheel = nextColorWheel(),
        highPassFilter = nextHighPassFilter(),
        sampleSize = nextPositiveInt(),
        interpolatedSampleSize = nextPositiveInt(),
        magnitudeEstimationStrategy = nextMagnitudeEstimationStrategy(),
        magnitudeMultiplier = Random.nextFloat(),
        millisecondsToWaitBetweenCheckingForNewAudio = nextPositiveLong()
    )
}