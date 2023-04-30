package config

import config.children.Client
import config.children.ColorWheel
import config.children.HighPassFilter
import config.children.MagnitudeEstimationStrategy

interface Config {
    var startAutomatically: Boolean
    val clients: List<Client>
    val colorWheel: ColorWheel
    val highPassFilter: HighPassFilter
    val sampleSize: Int
    val interpolatedSampleSize: Int
    val magnitudeEstimationStrategy: MagnitudeEstimationStrategy
    val magnitudeMultiplier: Float
    val millisecondsToWaitBetweenCheckingForNewAudio: Long
}