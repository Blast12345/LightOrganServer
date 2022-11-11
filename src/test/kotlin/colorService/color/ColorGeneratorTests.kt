package colorService.color

import colorService.sound.FrequencyBin
import org.junit.Assert.assertEquals
import org.junit.Test
import java.awt.Color

class ColorGeneratorTests {

    private val black = Color.black
    private val red = Color.red
    private val oppositeOfRed = Color.cyan

    private fun createSUT(): ColorGenerator {
        return ColorGenerator()
    }

    @Test
    fun `color is black when there are no frequency bins`() {
        val sut = createSUT()
        val actualColor = sut.colorForFrequency(emptyList())
        assertEquals(black, actualColor)
    }

    @Test
    fun `color is red when the dominant frequency is at the start of a range`() {
        val sut = createSUT()

        val frequencyBins = listOf(
            FrequencyBin(0.0, 1.0),
            FrequencyBin(50.0, 0.0),
            FrequencyBin(100.0, 0.0)
        )
        val actualColor = sut.colorForFrequency(frequencyBins)

        assertEquals(red, actualColor)
    }

    @Test
    fun `color is the opposite of red when the dominant frequency is in the middle of a range`() {
        val sut = createSUT()

        val frequencyBins = listOf(
            FrequencyBin(0.0, 0.0),
            FrequencyBin(50.0, 1.0),
            FrequencyBin(100.0, 0.0)
        )
        val actualColor = sut.colorForFrequency(frequencyBins)

        assertEquals(oppositeOfRed, actualColor)
    }

    @Test
    fun `color wraps around to red when the dominant frequency is at the end of a range`() {
        val sut = createSUT()

        val frequencyBins = listOf(
            FrequencyBin(0.0, 0.0),
            FrequencyBin(50.0, 0.0),
            FrequencyBin(100.0, 1.0)
        )
        val actualColor = sut.colorForFrequency(frequencyBins)

        assertEquals(red, actualColor)
    }

}