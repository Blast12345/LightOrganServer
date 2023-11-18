package color.scales

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HueScaleTests {

    private fun createSUT(): HueScale {
        return HueScale()
    }


    @Test
    fun `-1_75 is scaled to 0_25`() {
        val sut = createSUT()

        val actual = sut.scale(-1.75F)

        assertEquals(0.25F, actual)
    }

    @Test
    fun `-0_25 is scaled to 0_75`() {
        val sut = createSUT()

        val actual = sut.scale(-0.25F)

        assertEquals(0.75F, actual)
    }

    @Test
    fun `0 is scaled to 0`() {
        val sut = createSUT()

        val actual = sut.scale(0F)

        assertEquals(0F, actual)
    }

    @Test
    fun `0_5 is scaled to 0_5`() {
        val sut = createSUT()

        val actual = sut.scale(0.5F)

        assertEquals(0.5F, actual)
    }

    @Test
    fun `0_75 is scaled to 0_75`() {
        val sut = createSUT()

        val actual = sut.scale(0.75F)

        assertEquals(0.75F, actual)
    }

    @Test
    fun `2 is scaled to 0`() {
        val sut = createSUT()

        val actual = sut.scale(2F)

        assertEquals(0F, actual)
    }

}