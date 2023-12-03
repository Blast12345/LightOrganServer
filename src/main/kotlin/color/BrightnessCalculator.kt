package color

import sound.frequencyBins.FrequencyBinList

class BrightnessCalculator {

    // TODO: Improve logic around really low values. There is a lot of flickering.
    fun calculate(frequencyBins: FrequencyBinList): Float? {
        val magnitude = frequencyBins.maxOfOrNull { it.magnitude } ?: return null

        return if (magnitude < 1F) {
            magnitude
        } else {
            1F
        }
    }

    // TODO: Filter
//    private fun getFilteredBins(frequencyBins: FrequencyBinList): FrequencyBinList {
//        return bandPassFilter.filter(
//            frequencyBinList = frequencyBins,
//            lowCrossover = lowCrossover,
//            highCrossover = highCrossover
//        )
//    }

}

