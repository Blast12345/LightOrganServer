package sound.signalProcessor.interpolator

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray
import toolkit.monkeyTest.nextPositiveInt

class ZeroPaddingInterpolatorTests {

    private val samples = nextDoubleArray()
    private val algorithm: ZeroPaddingInterpolationAlgorithm = mockk()
    private val algorithmSamples = nextDoubleArray()
    private val corrector: ZeroPaddingInterpolationCorrector = mockk()
    private val correctedSamples = nextDoubleArray()
    private val desiredSize = nextPositiveInt()

    private fun createSUT(): ZeroPaddingInterpolator {
        return ZeroPaddingInterpolator(
            algorithm = algorithm,
            corrector = corrector,
            desiredSize = desiredSize
        )
    }

    @Test
    fun `interpolate samples to a given size with magnitude correction`() {
        val sut = createSUT()
        every { algorithm.applyTo(samples, desiredSize) } returns algorithmSamples
        every { corrector.correct(algorithmSamples, samples.size) } returns correctedSamples

        val actual = sut.interpolate(samples)

        assertArrayEquals(correctedSamples, actual, 0.001)
    }

}