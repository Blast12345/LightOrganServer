package gui.dashboard.tiles.Statistics

import config.Config
import javax.sound.sampled.AudioFormat

class StatisticsViewModelFactory(
    private val secondsOfAudioUsedCalculator: SecondsOfAudioUsedCalculator = SecondsOfAudioUsedCalculator(),
    private val lowestDiscernibleFrequencyCalculator: LowestDiscernibleFrequencyCalculator = LowestDiscernibleFrequencyCalculator(),
    private val frequencyResolutionCalculator: FrequencyResolutionCalculator = FrequencyResolutionCalculator()
) {

    fun create(audioFormat: AudioFormat, config: Config): StatisticsViewModel {
        // TODO: Are frequency resolution and lowest discernible frequency the same? If so, does it make sense to still differentiate them?
        val secondsOfAudioUsed = calculateSecondsOfAudioUsed(audioFormat, config)
        val lowestDiscernibleFrequency = calculateLowestDiscernibleFrequency(secondsOfAudioUsed)
        val frequencyResolutionCalculator = calculateFrequencyResolution(audioFormat, config)

        return StatisticsViewModel(
            durationOfAudioUsed = formatted(secondsOfAudioUsed, "seconds"),
            lowestDiscernibleFrequency = formatted(lowestDiscernibleFrequency, "Hz"),
            frequencyResolution = formatted(frequencyResolutionCalculator, "Hz")
        )
    }

    private fun calculateSecondsOfAudioUsed(audioFormat: AudioFormat, config: Config): Float {
        return secondsOfAudioUsedCalculator.calculate(
            sampleRate = audioFormat.sampleRate,
            sampleSize = config.sampleSize,
            numberOfChannels = audioFormat.channels
        )
    }

    private fun calculateLowestDiscernibleFrequency(secondsOfAudioUsed: Float): Float {
        return lowestDiscernibleFrequencyCalculator.calculate(
            secondsOfAudioUsed = secondsOfAudioUsed
        )
    }

    private fun calculateFrequencyResolution(audioFormat: AudioFormat, config: Config): Float {
        return frequencyResolutionCalculator.calculate(
            sampleSize = config.sampleSize,
            sampleRate = audioFormat.sampleRate,
            numberOfChannels = audioFormat.channels
        )
    }

    private fun formatted(value: Float, unitOfMeasure: String): String {
        val formattedValue = String.format("%.2f", value)
        return "$formattedValue $unitOfMeasure"
    }

}