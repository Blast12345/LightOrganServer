package sound.signalProcessing.hannFilter

import kotlin.math.PI
import kotlin.math.cos

// Reference: https://dsp.stackexchange.com/questions/19776/is-it-necessary-to-apply-some-window-method-to-obtain-the-fft-java
class StandardHannFilter : HannFilter {

    override fun filter(signal: DoubleArray): DoubleArray {
        val output = DoubleArray(signal.size)

        for (i in signal.indices) {
            val multiplier = 0.5 * (1 - cos(2 * PI * i / (signal.size - 1)))
            output[i] = multiplier * signal[i]
        }

        return output
    }

}