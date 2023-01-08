package color.dominantFrequency

import sound.frequencyBins.FrequencyBinList

interface MagnitudeEstimatorInterface {
    fun estimate(frequency: Float, frequencyBins: FrequencyBinList): Float
}

class MagnitudeEstimator : MagnitudeEstimatorInterface {

    override fun estimate(frequency: Float, frequencyBins: FrequencyBinList): Float {
        TODO("Not yet implemented")
    }

}