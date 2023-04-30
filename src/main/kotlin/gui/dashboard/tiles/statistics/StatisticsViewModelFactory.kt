package gui.dashboard.tiles.statistics

import LightOrganStateMachine
import androidx.compose.runtime.mutableStateOf
import config.PersistedConfig
import javax.sound.sampled.AudioFormat

class StatisticsViewModelFactory(
    private val secondsOfAudioUsedCalculator: SecondsOfAudioUsedCalculator = SecondsOfAudioUsedCalculator(),
    private val lowestDiscernibleFrequencyCalculator: LowestDiscernibleFrequencyCalculator = LowestDiscernibleFrequencyCalculator(),
    private val frequencyResolutionCalculator: FrequencyResolutionCalculator = FrequencyResolutionCalculator()
) {

    fun create(lightOrganStateMachine: LightOrganStateMachine, persistedConfig: PersistedConfig): StatisticsViewModel {
        // TODO: Are frequency resolution and lowest discernible frequency the same? If so, does it make sense to still differentiate them?
        val audioFormat = lightOrganStateMachine.input.audioFormat
        val secondsOfAudioUsed = calculateSecondsOfAudioUsed(audioFormat, persistedConfig)
        val lowestDiscernibleFrequency = calculateLowestDiscernibleFrequency(secondsOfAudioUsed)
        val frequencyResolutionCalculator = calculateFrequencyResolution(audioFormat, persistedConfig)

        return StatisticsViewModel(
            durationOfAudioUsed = mutableStateOf(formatted(secondsOfAudioUsed * 1000, "ms")),
            lowestDiscernibleFrequency = mutableStateOf(formatted(lowestDiscernibleFrequency, "Hz")),
            frequencyResolution = mutableStateOf(formatted(frequencyResolutionCalculator, "Hz"))
        )
    }

    private fun calculateSecondsOfAudioUsed(audioFormat: AudioFormat, persistedConfig: PersistedConfig): Float {
        return secondsOfAudioUsedCalculator.calculate(
            sampleRate = audioFormat.sampleRate,
            sampleSize = persistedConfig.sampleSize,
            numberOfChannels = audioFormat.channels
        )
    }

    private fun calculateLowestDiscernibleFrequency(secondsOfAudioUsed: Float): Float {
        return lowestDiscernibleFrequencyCalculator.calculate(
            secondsOfAudioUsed = secondsOfAudioUsed
        )
    }

    private fun calculateFrequencyResolution(audioFormat: AudioFormat, persistedConfig: PersistedConfig): Float {
        return frequencyResolutionCalculator.calculate(
            sampleSize = persistedConfig.sampleSize,
            sampleRate = audioFormat.sampleRate,
            numberOfChannels = audioFormat.channels
        )
    }

    private fun formatted(value: Float, unitOfMeasure: String): String {
        val formattedValue = String.format("%.2f", value)
        return "$formattedValue $unitOfMeasure"
    }

}