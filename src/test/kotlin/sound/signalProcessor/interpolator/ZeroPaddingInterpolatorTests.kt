package sound.signalProcessor.interpolator

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextPositiveInt

class ZeroPaddingInterpolatorTests {

    private val samples = doubleArrayOf()
    private val algorithm: ZeroPaddingInterpolationAlgorithm = mockk()
    private val algorithmSamples = doubleArrayOf()
    private val corrector: ZeroPaddingInterpolationCorrector = mockk()
    private val correctedSamples = doubleArrayOf()
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