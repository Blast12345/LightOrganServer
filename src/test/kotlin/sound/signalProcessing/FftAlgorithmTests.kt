package sound.signalProcessing

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.sin

class FftAlgorithmTests {

    private val signalFrequency = 50
    private val sampleRate = 44100
    private val signal = createSignal(signalFrequency)

    private fun createSUT(): FftAlgorithm {
        return FftAlgorithm()
    }

    @Test
    fun `apply the fft algorithm`() {
        val sut = createSUT()
        val magnitudes = sut.calculateMagnitudes(signal)
        val frequencyOfGreatestMagnitude = frequencyOfGreatestMagnitude(magnitudes)
        assertEquals(signalFrequency, frequencyOfGreatestMagnitude)
    }


    // Reference: the "Make 50 hz signal" section from https://github.com/wendykierp/JTransforms/issues/4#issuecomment-199352683
    private fun createSignal(frequency: Int): DoubleArray {
        val signal = DoubleArray(sampleRate)

        for (i in signal.indices) {
            val t = i * (1 / sampleRate.toDouble())
            signal[i] = sin(2 * Math.PI * frequency * t)
        }

        return signal
    }

    private fun frequencyOfGreatestMagnitude(magnitudes: DoubleArray): Int? {
        val greatestMagnitude = greatestMagnitudeIn(magnitudes)

        return if (greatestMagnitude != null) {
            magnitudes.indexOfFirst { it == greatestMagnitude }
        } else {
            null
        }
    }

    private fun greatestMagnitudeIn(magnitudes: DoubleArray): Double? {
        return magnitudes.maxOrNull()
    }

}