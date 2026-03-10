package bins

import kotlin.math.log2
import kotlin.math.pow

// TODO: Test me
class LowPassFilter(
    val frequency: Float,
    val slope: Float,
    val thresholdDb: Float = 48f // ENHANCEMENT: Make configurable or infer from bit depth
) {

    fun trim(bins: FrequencyBins): FrequencyBins {
        val hardCutoff = frequency * 2f.pow(thresholdDb / slope)
        return bins.filter { it.frequency <= hardCutoff }
    }

    fun filter(bins: FrequencyBins): FrequencyBins {
        return bins.map { filter(it) }
    }

    private fun filter(bin: FrequencyBin): FrequencyBin {
        val octaves = log2(bin.frequency / frequency)
        if (octaves <= 0f) return bin
        val attenuationDb = slope * octaves
        val multiplier = 10f.pow(-attenuationDb / 20f)
        return bin.copy(magnitude = bin.magnitude * multiplier)
    }

}