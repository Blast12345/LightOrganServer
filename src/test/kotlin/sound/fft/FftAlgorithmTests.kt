package sound.fft

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
        val amplitudes = sut.process(signal)
        val frequencyOfGreatestAmplitude = frequencyOfGreatestAmplitude(amplitudes)
        assertEquals(signalFrequency, frequencyOfGreatestAmplitude)
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

    private fun frequencyOfGreatestAmplitude(amplitudes: DoubleArray): Int? {
        val greatestAmplitude = greatestAmplitudeIn(amplitudes)

        return if (greatestAmplitude != null) {
            amplitudes.indexOfFirst { it == greatestAmplitude }
        } else {
            null
        }
    }

    private fun greatestAmplitudeIn(amplitudes: DoubleArray): Double? {
        return amplitudes.maxOrNull()
    }

}