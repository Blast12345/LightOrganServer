package sound.octave

class OctaveWeightedAverageCalculator {

    fun calculate(octaveBins: OctaveBinList): Float? {
        val weightedMagnitude = weightedMagnitude(octaveBins)
        val totalMagnitude = totalMagnitude(octaveBins)

        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
    }

    private fun weightedMagnitude(octaveBins: OctaveBinList): Float {
        var weightedMagnitude = 0F

        for (octaveBin in octaveBins) {
            weightedMagnitude += octaveBin.position * octaveBin.magnitude
        }

        return weightedMagnitude
    }

    private fun totalMagnitude(octaveBins: OctaveBinList): Float {
        return octaveBins.map { it.magnitude }.sum()
    }

}


//    private val octaveBins = listOf(
//        OctaveBin(0.0F, 98F),
//        OctaveBin(0.5F, 1F),
//        OctaveBin(1.0F, 1F)
//
//    assertEquals(0.015F, actual)