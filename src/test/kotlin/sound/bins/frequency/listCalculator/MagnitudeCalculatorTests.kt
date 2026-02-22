package sound.bins.frequency.listCalculator

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import sound.signalProcessor.fft.RelativeMagnitudeListCalculator
import sound.signalProcessor.fft.RelativeMagnitudeListNormalizer
import toolkit.monkeyTest.nextDoubleArray

class MagnitudeListCalculatorTests {

    private val samples = nextDoubleArray()
    private val relativeMagnitudeListCalculator: RelativeMagnitudeListCalculator = mockk()
    private val relativeMagnitudeList = nextDoubleArray()
    private val relativeMagnitudeListNormalizer: RelativeMagnitudeListNormalizer = mockk()
    private val normalizedMagnitudes = nextDoubleArray()

    private fun createSUT(): MagnitudeListCalculator {
        return MagnitudeListCalculator(
            relativeMagnitudeListCalculator = relativeMagnitudeListCalculator,
            relativeMagnitudeListNormalizer = relativeMagnitudeListNormalizer
        )
    }

    @Test
    fun `calculate the magnitudes for given samples`() {
        val sut = createSUT()
        every { relativeMagnitudeListCalculator.calculate(samples) } returns relativeMagnitudeList
        every {
            relativeMagnitudeListNormalizer.normalize(
                relativeMagnitudeList,
                samples.size
            )
        } returns normalizedMagnitudes

        val actual = sut.calculate(samples)

        assertArrayEquals(normalizedMagnitudes, actual)
    }
}
