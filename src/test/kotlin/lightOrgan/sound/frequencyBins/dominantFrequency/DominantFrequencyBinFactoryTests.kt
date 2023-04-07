package lightOrgan.sound.frequencyBins.dominantFrequency

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import lightOrgan.sound.frequencyBins.FrequencyBin
import lightOrgan.sound.frequencyBins.dominantFrequency.frequency.DominantFrequencyCalculatorInterface
import lightOrgan.sound.frequencyBins.dominantFrequency.magnitude.MagnitudeEstimator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class DominantFrequencyBinFactoryTests {

    private var dominantFrequencyCalculator: DominantFrequencyCalculatorInterface = mockk()
    private var magnitudeEstimator: MagnitudeEstimator = mockk()

    private val frequencyBins = nextFrequencyBins()

    private val dominantFrequency = Random.nextFloat()
    private val estimatedMagnitude = Random.nextFloat()

    @BeforeEach
    fun setup() {
        every { dominantFrequencyCalculator.calculate(frequencyBins) } returns dominantFrequency
        every { magnitudeEstimator.estimate(dominantFrequency, frequencyBins) } returns estimatedMagnitude
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DominantFrequencyBinFactory {
        return DominantFrequencyBinFactory(
            dominantFrequencyCalculator = dominantFrequencyCalculator,
            magnitudeEstimator = magnitudeEstimator
        )
    }

    @Test
    fun `return the dominant frequency bin`() {
        val sut = createSUT()
        val frequencyBin = sut.create(frequencyBins)
        val expected = FrequencyBin(dominantFrequency, estimatedMagnitude)
        assertEquals(expected, frequencyBin)
    }

    @Test
    fun `return null when there is no dominant frequency`() {
        val sut = createSUT()
        every { dominantFrequencyCalculator.calculate(any()) } returns null
        val frequencyBin = sut.create(frequencyBins)
        assertNull(frequencyBin)
    }

    @Test
    fun `return null when there is no estimated magnitude`() {
        val sut = createSUT()
        every { dominantFrequencyCalculator.calculate(any()) } returns null
        val frequencyBin = sut.create(frequencyBins)
        assertNull(frequencyBin)
    }

}