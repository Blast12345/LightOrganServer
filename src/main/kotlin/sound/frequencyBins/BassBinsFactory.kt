package sound.frequencyBins

import input.audioFrame.AudioFrame

class BassBinsFactory {

    fun create(audioFrame: AudioFrame): FrequencyBinList {
        return listOf()
    }


//    private fun getBassRegionBins(frequencyBins: FrequencyBinList): FrequencyBinList {
//        return passBandRegionFilter.filter(
//            frequencyBinList = frequencyBins,
//            lowStopFrequency = lowCrossover.stopFrequency,
//            highStopFrequency = highCrossover.stopFrequency
//        )
//    }

}