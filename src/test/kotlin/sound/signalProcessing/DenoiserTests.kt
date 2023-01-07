package sound.signalProcessing

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class DenoiserTests {

    private val magnitude1 = 0.1
    private val magnitude2 = 0.01
    private val magnitude3 = 0.001
    private val magnitude4 = 0.0001

    private fun createSUT(): Denoiser {
        return Denoiser()
    }

    @Test
    fun `denoise magnitudes by subtracting one-thousandth of an magnitude`() {
        // NOTE: This value is fairly arbitrary. Different setups will have different
        // levels of interference, so I just simply made a decision that made sense
        // for me
        val sut = createSUT()
        val magnitudes = doubleArrayOf(magnitude1, magnitude2, magnitude3)
        val denoisedMagnitudes = sut.denoise(magnitudes)
        val expectedMagnitudes = doubleArrayOf(0.099, 0.009, 0.0)
        assertArrayEquals(expectedMagnitudes, denoisedMagnitudes, 0.001)
    }

    @Test
    fun `denoised magnitudes cannot be less than 0`() {
        val sut = createSUT()
        val magnitudes = doubleArrayOf(magnitude4)
        val denoisedMagnitudes = sut.denoise(magnitudes)
        val expectedMagnitudes = doubleArrayOf(0.0)
        assertArrayEquals(expectedMagnitudes, denoisedMagnitudes, 0.001)
    }

}