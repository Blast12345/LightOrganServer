package math.featureScaling

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MinMaxNormalizationTests {

    private fun createSUT(): MinMaxNormalization {
        return MinMaxNormalization()
    }

    @Test
    fun `given a range of 0 to 4, 0 is normalized to 0`() {
        val sut = createSUT()

        val actual = sut.normalize(
            value = 0F,
            minimum = 0F,
            maximum = 4F
        )

        assertEquals(0F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 4 is normalized to 1`() {
        val sut = createSUT()

        val actual = sut.normalize(
            value = 4F,
            minimum = 0F,
            maximum = 4F
        )

        assertEquals(1F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 2 is normalized to 0_5`() {
        val sut = createSUT()

        val actual = sut.normalize(
            value = 2F,
            minimum = 0F,
            maximum = 4F
        )

        assertEquals(0.5F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 0 is scaled to 0`() {
        val sut = createSUT()

        val actual = sut.denormalize(
            value = 0F,
            minimum = 0F,
            maximum = 4F
        )

        assertEquals(0F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 1 is scaled to 4`() {
        val sut = createSUT()

        val actual = sut.denormalize(
            value = 1F,
            minimum = 0F,
            maximum = 4F
        )

        assertEquals(4F, actual)
    }

    @Test
    fun `given a range of 0 to 4, 0_5 is scaled to 2`() {
        val sut = createSUT()

        val actual = sut.denormalize(
            value = 0.5F,
            minimum = 0F,
            maximum = 4F
        )

        assertEquals(2F, actual)
    }

}