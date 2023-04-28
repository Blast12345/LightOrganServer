package gui.dashboard.tiles.statistics

class SecondsOfAudioUsedCalculator {

    fun calculate(sampleSize: Int, sampleRate: Float, numberOfChannels: Int): Float {
        return sampleSize.toFloat() / sampleRate / numberOfChannels.toFloat()
    }

}
