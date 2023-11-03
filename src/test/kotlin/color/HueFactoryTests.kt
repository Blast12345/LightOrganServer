package color

import color.stevensPowerLaw.HueScale
import color.stevensPowerLaw.LogarithmicRescaler
import color.stevensPowerLaw.MusicScale
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class HueFactoryTests {

    private val frequency = Random.nextFloat()
    private val rescaler: LogarithmicRescaler = mockk()
    private val hue = Random.nextFloat()

    private fun createSUT(): HueFactory {
        return HueFactory(
            rescaler = rescaler
        )
    }

    @Test
    fun `calculate the hue with respect to Stevens Power Law`() {
        val sut = createSUT()
        every { rescaler.rescale(frequency, MusicScale, HueScale) } returns hue

        val actual = sut.create(frequency)

        assertEquals(hue, actual)
    }

}