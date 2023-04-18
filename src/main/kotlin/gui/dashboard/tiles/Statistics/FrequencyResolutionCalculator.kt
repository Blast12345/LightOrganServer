package gui.dashboard.tiles.Statistics

class FrequencyResolutionCalculator {

    // TODO: Verify this calculation
    fun calculate(sampleRate: Float, sampleSize: Int, numberOfChannels: Int): Float {
        return sampleRate / sampleSize.toFloat() * numberOfChannels.toFloat()
    }

}