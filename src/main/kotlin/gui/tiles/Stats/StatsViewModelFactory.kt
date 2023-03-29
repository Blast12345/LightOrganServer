package gui.tiles.Stats

import config.Config
import sound.input.Input

class StatsViewModelFactory {

    fun create(input: Input?, config: Config): StatsViewModel {
        if (input == null) {
            return StatsViewModel()
        }

        // TODO: For unit testing, there would be calculator for each value
        // TODO: Take number of channels into account, otherwise it looks like accuracy is higher than it really is
        val secondsOfAudioUsed = config.sampleSize / input.dataLine.format.sampleRate
        val lowestDiscernibleFrequency = 1 / secondsOfAudioUsed
        val frequencyResolution = input.dataLine.format.sampleRate / config.sampleSize

        return StatsViewModel(
            durationOfAudioUsed = formatted(secondsOfAudioUsed, "seconds"),
            lowestDiscernibleFrequency = formatted(lowestDiscernibleFrequency, "Hz"),
            frequencyResolution = formatted(frequencyResolution, "Hz")
        )
    }

    private fun formatted(value: Float, unitOfMeasure: String): String {
        val formattedValue = String.format("%.2f", value)
        return "$formattedValue $unitOfMeasure"
    }

}