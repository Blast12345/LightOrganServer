package lightOrgan.color

import config.children.ColorWheel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import lightOrgan.sound.frequencyBins.FrequencyBin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HueFactoryTests {

    private val colorWheel: ColorWheel = mockk()
    private val startingFrequency: Float = 40F
    private val endingFrequency: Float = 120F
    private val offset: Float = 0.25F

    @BeforeEach
    fun setup() {
        every { colorWheel.startingFrequency } returns startingFrequency
        every { colorWheel.endingFrequency } returns endingFrequency
        every { colorWheel.offset } returns offset
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): HueFactory {
        return HueFactory(
            colorWheel = colorWheel
        )
    }

    @Test
    fun `return the frequency's relative position in the color wheel`() {
        val sut = createSUT()
        val frequencyBin = FrequencyBin(60F, 0F)
        val hue = sut.create(frequencyBin)
        assertEquals(0.5F, hue)
    }

}