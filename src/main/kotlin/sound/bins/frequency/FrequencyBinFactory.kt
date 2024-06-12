package sound.bins.frequency

class FrequencyBinFactory {

    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat()
        )
    }

}
