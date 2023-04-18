package gui.dashboard.tiles.Statistics

class LowestDiscernibleFrequencyCalculator {

    fun calculate(secondsOfAudioUsed: Float): Float {
        return 1 / secondsOfAudioUsed
    }

}