package config

import config.children.Client
import config.children.ColorWheel
import config.children.HighPassFilter
import config.children.MagnitudeEstimationStrategy

data class FakeConfig(
    override var startAutomatically: Boolean,
    override val clients: List<Client>,
    override val colorWheel: ColorWheel,
    override val highPassFilter: HighPassFilter,
    override val sampleSize: Int,
    override val interpolatedSampleSize: Int,
    override val magnitudeEstimationStrategy: MagnitudeEstimationStrategy,
    override val magnitudeMultiplier: Float,
    override val millisecondsToWaitBetweenCheckingForNewAudio: Long
) : Config