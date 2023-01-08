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
import sound.frequencyBins.FrequencyBin
import toolkit.monkeyTest.nextFrequencyBins

class HueFactoryTests {

    private var minimumFrequencyFinder: MinimumFrequencyFinder = mockk()
    private var maximumFrequencyFinder: MaximumFrequencyFinder = mockk()

    private val dominantFrequencyBin = FrequencyBin(75F, 0F)
    private val frequencyBins = nextFrequencyBins()

    private val minimumFrequency = 50F
    private val maximumFrequency = 100F

    @BeforeEach
    fun setup() {
        every { minimumFrequencyFinder.find(any()) } returns minimumFrequency
        every { maximumFrequencyFinder.find(any()) } returns maximumFrequency
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): HueFactory {
        return HueFactory(
            minimumFrequencyFinder = minimumFrequencyFinder,
            maximumFrequencyFinder = maximumFrequencyFinder
        )
    }

    @Test
    fun `the hue is corresponds to the position of the dominant frequency within the provided range`() {
        val sut = createSUT()
        val hue = sut.create(dominantFrequencyBin, frequencyBins)
        assertEquals(0.5F, hue)
        verify { minimumFrequencyFinder.find(frequencyBins) }
        verify { maximumFrequencyFinder.find(frequencyBins) }
    }

    @Test
    fun `the hue is null when the minimum frequency is null`() {
        val sut = createSUT()
        every { minimumFrequencyFinder.find(any()) } returns null
        val actualHue = sut.create(dominantFrequencyBin, frequencyBins)
        assertNull(actualHue)
    }

    @Test
    fun `the hue is null when the maximum frequency is null`() {
        val sut = createSUT()
        every { maximumFrequencyFinder.find(any()) } returns null
        val actualHue = sut.create(dominantFrequencyBin, frequencyBins)
        assertNull(actualHue)
    }

    @Test
    fun `the hue is null when the maximum frequency is 0`() {
        val sut = createSUT()
        every { maximumFrequencyFinder.find(any()) } returns 0F
        val actualHue = sut.create(dominantFrequencyBin, frequencyBins)
        assertNull(actualHue)
    }

}