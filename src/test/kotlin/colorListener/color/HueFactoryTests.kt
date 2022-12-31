package colorListener.color

import sound.frequencyBins.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sound.frequencyBins.FrequencyBin

class HueFactoryTests {

    private lateinit var averageFrequencyFactory: FakeAverageFrequencyFactory
    private lateinit var minimumFrequencyFactory: FakeMinimumFrequencyFactory
    private lateinit var maximumFrequencyFactory: FakeMaximumFrequencyFactory
    private val frequencyBins: FrequencyBins = listOf(FrequencyBin(75.0, 1.2))

    @Before
    fun setup() {
        averageFrequencyFactory = FakeAverageFrequencyFactory()
        minimumFrequencyFactory = FakeMinimumFrequencyFactory()
        maximumFrequencyFactory = FakeMaximumFrequencyFactory()
    }

    private fun createSUT(): HueFactory {
        return HueFactory(
            averageFrequencyFactory,
            minimumFrequencyFactory,
            maximumFrequencyFactory
        )
    }

    @Test
    fun `the hue is corresponds to where the average frequency falls within the provided range`() {
        val sut = createSUT()
        averageFrequencyFactory.frequency = 75F
        minimumFrequencyFactory.frequency = 10F
        maximumFrequencyFactory.frequency = 100F
        val actualHue = sut.hueFrom(frequencyBins)
        assertEquals(0.65f, actualHue!!, 0.001f)
        assertEquals(frequencyBins, averageFrequencyFactory.frequencyBins)
        assertEquals(frequencyBins, minimumFrequencyFactory.frequencyBins)
        assertEquals(frequencyBins, maximumFrequencyFactory.frequencyBins)
    }

    @Test
    fun `the hue is null when the maximum frequency is 0`() {
        val sut = createSUT()
        maximumFrequencyFactory.frequency = 0F
        val actualHue = sut.hueFrom(frequencyBins)
        assertNull(actualHue)
    }

    @Test
    fun `the hue is null when the maximum frequency is null`() {
        val sut = createSUT()
        maximumFrequencyFactory.frequency = null
        val actualHue = sut.hueFrom(frequencyBins)
        assertNull(actualHue)
    }

    @Test
    fun `the hue is null when the minimum frequency is null`() {
        val sut = createSUT()
        minimumFrequencyFactory.frequency = null
        val actualHue = sut.hueFrom(frequencyBins)
        assertNull(actualHue)
    }

    @Test
    fun `the hue is null when the average frequency is null`() {
        val sut = createSUT()
        averageFrequencyFactory.frequency = null
        val actualHue = sut.hueFrom(frequencyBins)
        assertNull(actualHue)
    }

}