package sound.frequencyBins

interface FrequencyBinFactoryInterface {
    fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin
}

// TODO: Test me
class FrequencyBinFactory : FrequencyBinFactoryInterface {

    override fun create(index: Int, granularity: Float, magnitude: Double): FrequencyBin {
        return FrequencyBin(
            frequency = index * granularity,
            magnitude = magnitude.toFloat()
        )
    }

}