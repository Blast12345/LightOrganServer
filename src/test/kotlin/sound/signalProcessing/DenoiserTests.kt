package sound.signalProcessing

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class DenoiserTests {

    private val amplitude1 = 0.1
    private val amplitude2 = 0.01
    private val amplitude3 = 0.001
    private val amplitude4 = 0.0001

    private fun createSUT(): Denoiser {
        return Denoiser()
    }

    @Test
    fun `denoise amplitudes by subtracting one-thousandth of an amplitude`() {
        // NOTE: This value is fairly arbitrary. Different setups will have different
        // levels of interference, so I just simply made a decision that made sense
        // for me
        val sut = createSUT()
        val amplitudes = doubleArrayOf(amplitude1, amplitude2, amplitude3)
        val denoisedAmplitudes = sut.denoise(amplitudes)
        val expectedAmplitudes = doubleArrayOf(0.099, 0.009, 0.0)
        assertArrayEquals(expectedAmplitudes, denoisedAmplitudes, 0.001)
    }

    @Test
    fun `denoised amplitudes cannot be less than 0`() {
        val sut = createSUT()
        val amplitudes = doubleArrayOf(amplitude4)
        val denoisedAmplitudes = sut.denoise(amplitudes)
        val expectedAmplitudes = doubleArrayOf(0.0)
        assertArrayEquals(expectedAmplitudes, denoisedAmplitudes, 0.001)
    }

}