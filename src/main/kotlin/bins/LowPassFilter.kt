package bins

import audio.samples.AudioFrame
import kotlin.math.pow
import kotlin.math.roundToInt

// TODO: Test me
class LowPassFilter(
    val frequency: Float,
    val slope: Float
) {

    fun trim(bins: FrequencyBins, thresholdDb: Float = 48f): FrequencyBins {
        val hardCutoff = frequency * 2f.pow(thresholdDb / slope)
        return bins.filter { it.frequency <= hardCutoff }
    }

    private var stages: List<Biquad> = emptyList()

    private fun buildStages(sampleRate: Float): List<Biquad> {
        val order = (slope / 6f).roundToInt().coerceAtLeast(2).let {
            if (it % 2 != 0) it + 1 else it
        }
        val numStages = order / 2

        return List(numStages) { k ->
            val q = butterworthQ(order, k)
            Biquad.lowPass(frequency.toDouble(), sampleRate.toDouble(), q)
        }
    }

    fun filter(audio: AudioFrame): AudioFrame {
        if (stages.isEmpty()) {
            stages = buildStages(audio.format.sampleRate)
        }

        var samples = audio.samples
        for (stage in stages) {
            samples = stage.process(samples)
        }
        return AudioFrame(samples, audio.format)
    }

    fun reset() = stages.forEach { it.reset() }

}