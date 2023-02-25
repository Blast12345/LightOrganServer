package sound.frequencyBins

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class GranularityCalculatorTests {

    private val nyquistFrequencyCalculator: NyquistFrequencyCalculatorInterface = mockk()

    private val numberOfBins = 50
    private val sampleRate = Random.nextFloat()
    private val numberOfChannels = 2

    private val nyquistFrequency = 100F

    @BeforeEach
    fun setup() {
        every { nyquistFrequencyCalculator.calculate(any()) } returns nyquistFrequency
    }

    private fun createSUT(): GranularityCalculator {
        return GranularityCalculator(
            nyquistFrequencyCalculator = nyquistFrequencyCalculator
        )
    }

    @Test
    fun `the granularity is the Nyquist Frequency divided by the number of bins times the number of channels`() {
        val sut = createSUT()
        val granularity = sut.calculate(numberOfBins, sampleRate, numberOfChannels)
        assertEquals(4F, granularity, 0.001F)
        verify { nyquistFrequencyCalculator.calculate(sampleRate) }
    }
}