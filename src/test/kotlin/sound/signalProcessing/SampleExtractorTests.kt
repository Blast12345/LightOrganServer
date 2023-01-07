package sound.signalProcessing

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioSignal
import kotlin.random.Random

class SampleExtractorTests {

    private var sampleSizeCalculator: SampleSizeCalculatorInterface = mockk()

    private val samples = doubleArrayOf(1.0, 2.0, 3.0)
    private val audioSignal = nextAudioSignal(samples)
    private val lowestFrequency = Random.nextFloat()

    private val newSize = 2

    @BeforeEach
    fun setup() {
        every { sampleSizeCalculator.calculate(any(), any()) } returns newSize
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): SampleExtractor {
        return SampleExtractor(
            sampleSizeCalculator = sampleSizeCalculator
        )
    }

    @Test
    fun `take the latest N samples that support the lowest frequency`() {
        val sut = createSUT()
        val extractedSamples = sut.extract(audioSignal, lowestFrequency)
        val expected = doubleArrayOf(2.0, 3.0)
        assertArrayEquals(expected, extractedSamples, 0.001)
        verify { sampleSizeCalculator.calculate(lowestFrequency, audioSignal.sampleRate) }
    }

}