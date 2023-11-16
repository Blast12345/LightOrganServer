package math.featureScaling

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LinearScaleTests {

    @Test
    fun `given a range of 0 to 4, 0 is normalized to 0`() {
        val sut = LinearScale(0F, 4F)

        val actual = sut.normalize(0F)

        assertEquals(0F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 4 is normalized to 1`() {
        val sut = LinearScale(0F, 4F)

        val actual = sut.normalize(4F)

        assertEquals(1F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 2 is normalized to 0_5`() {
        val sut = LinearScale(0F, 4F)

        val actual = sut.normalize(2F)

        assertEquals(0.5F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 0 is scaled to 0`() {
        val sut = LinearScale(0F, 4F)

        val actual = sut.scale(0F)

        assertEquals(0F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 1 is scaled to 4`() {
        val sut = LinearScale(0F, 4F)

        val actual = sut.scale(1F)

        assertEquals(4F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 0_5 is scaled to 2`() {
        val sut = LinearScale(0F, 4F)

        val actual = sut.scale(0.5F)

        assertEquals(2F, actual)
    }

}