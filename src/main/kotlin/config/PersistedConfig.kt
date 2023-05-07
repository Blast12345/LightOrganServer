package config

import config.children.Client
import config.children.ColorWheel
import config.children.HighPassFilter
import config.children.MagnitudeEstimationStrategy
import java.util.prefs.Preferences

class PersistedConfig(
    private val preferences: Preferences = Preferences.userRoot()
) : Config {

    override var startAutomatically: Boolean
        get() = preferences.getBoolean(startAutomaticallyKey, false)
        set(value) = preferences.putBoolean(startAutomaticallyKey, value)
    private val startAutomaticallyKey = "startAutomaticallyKey"

    override val clients: List<Client> = listOf()
    override val colorWheel: ColorWheel = ColorWheel(40F, 120F, 0.25F)
    override val highPassFilter: HighPassFilter = HighPassFilter(120F, 15F)
    override val sampleSize: Int = 4100
    override val interpolatedSampleSize: Int = 65536
    override val magnitudeEstimationStrategy: MagnitudeEstimationStrategy = MagnitudeEstimationStrategy(5)
    override val magnitudeMultiplier: Float = 1.25F
    override val millisecondsToWaitBetweenCheckingForNewAudio: Long = 1
}