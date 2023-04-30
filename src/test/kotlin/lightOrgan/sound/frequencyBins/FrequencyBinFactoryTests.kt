package lightOrgan.sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrequencyBinFactoryTests {

    private val magnitudeMultiplier = 2F

    private val index = 2
    private val granularity = 5F
    private val magnitude = 7.0

    private fun createSUT(): FrequencyBinFactory {
        return FrequencyBinFactory(
            magnitudeMultiplier = magnitudeMultiplier
        )
    }

    @Test
    fun `create a frequency bin`() {
        val sut = createSUT()
        val frequencyBin = sut.create(index, granularity, magnitude)
        val expected = FrequencyBin(10F, 14F)
        assertEquals(expected, frequencyBin)
    }

    @Test
    fun `the frequency is the index times granularity`() {
        val sut = createSUT()
        val actual = sut.create(index, granularity, magnitude)
        assertEquals(10F, actual.frequency)
    }

    @Test
    fun `the magnitude is the original magnitude times the multiplier`() {
        val sut = createSUT()
        val actual = sut.create(index, granularity, magnitude)
        assertEquals(14F, actual.magnitude)
    }

}