package gui.dashboard.tiles.statistics

class LowestDiscernibleFrequencyCalculator {

    fun calculate(secondsOfAudioUsed: Float): Float {
        return 1 / secondsOfAudioUsed
    }

}