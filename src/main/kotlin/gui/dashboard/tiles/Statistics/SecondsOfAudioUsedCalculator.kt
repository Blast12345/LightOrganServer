package gui.dashboard.tiles.Statistics

class SecondsOfAudioUsedCalculator {

    fun calculate(sampleSize: Int, sampleRate: Float, numberOfChannels: Int): Float {
        return sampleSize.toFloat() / sampleRate / numberOfChannels.toFloat()
    }

}
