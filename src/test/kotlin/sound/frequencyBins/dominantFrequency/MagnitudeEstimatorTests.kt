package sound.frequencyBins.dominantFrequency

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.finders.FrequencyBinFinderInterface
import toolkit.monkeyTest.nextFrequencyBin
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class MagnitudeEstimatorTests {

    private val frequencyBinFinder: FrequencyBinFinderInterface = mockk()
    private val magnitudeInterpolator: MagnitudeInterpolatorInterface = mockk()

    private val frequency = Random.nextFloat()
    private val frequencyBins = nextFrequencyBins()

    private val exactFrequencyBin = nextFrequencyBin()
    private val lowerNeighbor = nextFrequencyBin()
    private val higherNeighbor = nextFrequencyBin()
    private val interpolatedMagnitude = Random.nextFloat()

    @BeforeEach
    fun setup() {
        every { frequencyBinFinder.findExact(any(), any()) } returns null
        every { frequencyBinFinder.findLowerNeighbor(any(), any()) } returns null
        every { frequencyBinFinder.findHigherNeighbor(any(), any()) } returns null
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): MagnitudeEstimator {
        return MagnitudeEstimator(
            frequencyBinFinder = frequencyBinFinder,
            magnitudeInterpolator = magnitudeInterpolator
        )
    }

    @Test
    fun `return null when no nearby frequency bins can be found`() {
        val sut = createSUT()
        val estimate = sut.estimate(frequency, frequencyBins)
        assertNull(estimate)
    }

    @Test
    fun `return the exact magnitude when an exact match is found`() {
        val sut = createSUT()
        every { frequencyBinFinder.findExact(frequency, frequencyBins) } returns exactFrequencyBin
        val estimate = sut.estimate(frequency, frequencyBins)
        assertEquals(exactFrequencyBin.magnitude, estimate)
    }

    @Test
    fun `return the interpolated magnitude when both neighboring bins are found`() {
        val sut = createSUT()

        // @formatter:off
        every { frequencyBinFinder.findLowerNeighbor(frequency, frequencyBins) } returns lowerNeighbor
        every { frequencyBinFinder.findHigherNeighbor(frequency, frequencyBins) } returns higherNeighbor
        every { magnitudeInterpolator.interpolate(frequency, lowerNeighbor, higherNeighbor) } returns interpolatedMagnitude
        // @formatter:on

        val estimate = sut.estimate(frequency, frequencyBins)
        assertEquals(interpolatedMagnitude, estimate)
    }

    @Test
    fun `return the lower neighbors magnitude when only that neighbor is found`() {
        val sut = createSUT()
        every { frequencyBinFinder.findLowerNeighbor(frequency, frequencyBins) } returns lowerNeighbor
        val estimate = sut.estimate(frequency, frequencyBins)
        assertEquals(lowerNeighbor.magnitude, estimate)
    }

    @Test
    fun `return the higher neighbors magnitude when only that neighbor is found`() {
        val sut = createSUT()
        every { frequencyBinFinder.findHigherNeighbor(frequency, frequencyBins) } returns higherNeighbor
        val estimate = sut.estimate(frequency, frequencyBins)
        assertEquals(higherNeighbor.magnitude, estimate)
    }

}