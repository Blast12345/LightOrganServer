package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.AverageFrequencyCalculator
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.MaximumFrequencyFinder
import sound.frequencyBins.MinimumFrequencyFinder
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class HueFactoryTests {

    private var averageFrequencyCalculator: AverageFrequencyCalculator = mockk()
    private var minimumFrequencyFinder: MinimumFrequencyFinder = mockk()
    private var maximumFrequencyFinder: MaximumFrequencyFinder = mockk()
    private val frequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setup() {
        every { averageFrequencyCalculator.calculate(any()) } returns Random.nextFloat()
        every { minimumFrequencyFinder.find(any()) } returns Random.nextFloat()
        every { maximumFrequencyFinder.find(any()) } returns Random.nextFloat()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): HueFactory {
        return HueFactory(
            averageFrequencyCalculator = averageFrequencyCalculator,
            minimumFrequencyFinder = minimumFrequencyFinder,
            maximumFrequencyFinder = maximumFrequencyFinder
        )
    }

    @Test
    fun `the hue is corresponds to the position of the average frequency within the provided range`() {
        val sut = createSUT()
        every { averageFrequencyCalculator.calculate(frequencyBins) } returns 75F
        every { minimumFrequencyFinder.find(frequencyBins) } returns 50F
        every { maximumFrequencyFinder.find(frequencyBins) } returns 100F

        val actualHue = sut.create(frequencyBins)

        assertEquals(0.5F, actualHue!!, 0.001F)
    }

    @Test
    fun `the hue should only respect frequencies up to 120hz`() {
        val sut = createSUT()
        val bin1 = FrequencyBin(50F, 1F)
        val bin2 = FrequencyBin(100F, 1F)
        val bin3 = FrequencyBin(150F, 1F)
        val frequencyBins = listOf(bin1, bin2, bin3)

        sut.create(frequencyBins)

        val expectedFrequencyBins = listOf(bin1, bin2)
        verify { averageFrequencyCalculator.calculate(expectedFrequencyBins) }
        verify { minimumFrequencyFinder.find(expectedFrequencyBins) }
        verify { maximumFrequencyFinder.find(expectedFrequencyBins) }
    }

    @Test
    fun `the hue is null when the maximum frequency is 0`() {
        val sut = createSUT()
        every { maximumFrequencyFinder.find(any()) } returns 0F
        val actualHue = sut.create(frequencyBins)
        assertNull(actualHue)
    }

    @Test
    fun `the hue is null when the maximum frequency is null`() {
        val sut = createSUT()
        every { maximumFrequencyFinder.find(any()) } returns null
        val actualHue = sut.create(frequencyBins)
        assertNull(actualHue)
    }

    @Test
    fun `the hue is null when the minimum frequency is null`() {
        val sut = createSUT()
        every { minimumFrequencyFinder.find(any()) } returns null
        val actualHue = sut.create(frequencyBins)
        assertNull(actualHue)
    }

    @Test
    fun `the hue is null when the average frequency is null`() {
        val sut = createSUT()
        every { averageFrequencyCalculator.calculate(any()) } returns null
        val actualHue = sut.create(frequencyBins)
        assertNull(actualHue)
    }

}