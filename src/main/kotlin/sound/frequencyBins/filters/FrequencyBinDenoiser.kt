package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBin

interface FrequencyBinDenoiserInterface {
    fun denoise(frequencyBin: FrequencyBin): FrequencyBin
}

class FrequencyBinDenoiser : FrequencyBinDenoiserInterface {

    override fun denoise(frequencyBin: FrequencyBin): FrequencyBin {
        return FrequencyBin(
            frequency = frequencyBin.frequency,
            magnitude = getMagnitude(frequencyBin.magnitude)
        )
    }

    // ENHANCEMENT: There may be a more elegant method; perhaps subtracting the average of the 10 lowest bins?
    private fun getMagnitude(magnitude: Float): Float {
        return if (magnitude < 0.01) {
            0F
        } else {
            magnitude
        }
    }

}