package sound.bins.octave

class OctaveWeightedAverageCalculator {

    fun calculate(octaveBins: OctaveBins): Float? {
        val weightedMagnitude = weightedMagnitude(octaveBins)
        val totalMagnitude = totalMagnitude(octaveBins)

        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
    }

    private fun weightedMagnitude(octaveBins: OctaveBins): Float {
        var weightedMagnitude = 0F

        for (octaveBin in octaveBins) {
            weightedMagnitude += octaveBin.position * octaveBin.magnitude
        }

        return weightedMagnitude
    }

    private fun totalMagnitude(octaveBins: OctaveBins): Float {
        return octaveBins.map { it.magnitude }.sum()
    }

}

