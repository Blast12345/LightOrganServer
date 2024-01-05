package sound.bins.listCalculator

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import sound.bins.frequencyBins.listCalculator.MagnitudeListCalculator
import sound.signalProcessor.SignalProcessor
import sound.signalProcessor.fft.RelativeMagnitudeListCalculator
import sound.signalProcessor.fft.RelativeMagnitudeListNormalizer
import toolkit.monkeyTest.nextDoubleArray

class MagnitudeListCalculatorTests {

    private val samples = nextDoubleArray()
    private val signalProcessor: SignalProcessor = mockk()
    private val processedSamples = nextDoubleArray()
    private val relativeMagnitudeListCalculator: RelativeMagnitudeListCalculator = mockk()
    private val relativeMagnitudeList = nextDoubleArray()
    private val relativeMagnitudeListNormalizer: RelativeMagnitudeListNormalizer = mockk()
    private val normalizedMagnitudes = nextDoubleArray()

    private fun createSUT(): MagnitudeListCalculator {
        return MagnitudeListCalculator(
            signalProcessor = signalProcessor,
            relativeMagnitudeListCalculator = relativeMagnitudeListCalculator,
            relativeMagnitudeListNormalizer = relativeMagnitudeListNormalizer
        )
    }

    @Test
    fun `calculate the magnitudes for given samples`() {
        val sut = createSUT()
        every { signalProcessor.process(samples) } returns processedSamples
        every { relativeMagnitudeListCalculator.calculate(processedSamples) } returns relativeMagnitudeList
        every { relativeMagnitudeListNormalizer.normalize(relativeMagnitudeList, processedSamples.size) } returns normalizedMagnitudes

        val actual = sut.calculate(samples)

        assertArrayEquals(normalizedMagnitudes, actual)
    }
}