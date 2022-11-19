package colorListener.sound.frequencyBins

data class FrequencyBin(val frequency: Double, val amplitude: Double)

// TODO: Test me
fun List<FrequencyBin>.minimumFrequency(): Double {
    return first().frequency
}

fun List<FrequencyBin>.maximumFrequency(): Double {
    return last().frequency
}

fun List<FrequencyBin>.averageFrequency(): Double {
    // TODO: Handle case of 0 as total amplitude
    return weightedAmplitude() / totalAmplitude()
}

private fun List<FrequencyBin>.weightedAmplitude(): Double {
    var weightedAmplitude = 0.0

    for (frequencyBin in this) {
        weightedAmplitude += frequencyBin.frequency * frequencyBin.amplitude
    }

    return weightedAmplitude
}

private fun List<FrequencyBin>.totalAmplitude(): Double {
    return map { it.amplitude }.sum()
}