package sound.signalProcessor.filters.window

import kotlin.math.PI
import kotlin.math.cos

// Reference: https://dsp.stackexchange.com/questions/19776/is-it-necessary-to-apply-some-window-method-to-obtain-the-fft-java
class HannFilterAlgorithm : WindowFilter {

    @Suppress("MagicNumber")
    override fun applyTo(samples: DoubleArray): DoubleArray {
        // Hann
//        val output = DoubleArray(samples.size)
//
//        for (i in samples.indices) {
//            val multiplier = 0.5 * (1 - cos(2 * PI * i / (samples.size - 1)))
//            output[i] = multiplier * samples[i]
//        }
//
//        return output

        // Blackman
        val output = DoubleArray(samples.size)

        for (i in samples.indices) {
            val multiplier = 0.42 - 0.5 * cos(2 * PI * i / (samples.size - 1)) + 0.08 * cos(4 * PI * i / (samples.size - 1))
            output[i] = multiplier * samples[i]
        }

        return output
    }

}
