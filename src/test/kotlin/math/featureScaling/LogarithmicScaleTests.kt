package math.featureScaling

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LogarithmicScaleTests {

    @Test
    fun `given a scale of base 2 and a range of 20 to 40, 20 is normalized to 0`() {
        val sut = LogarithmicScale(20F, 40F, 2F)

        val actual = sut.normalize(20F)

        assertEquals(0F, actual)
    }

    @Test
    fun `given a scale of base 2 and a range of 20 to 40, 40 is normalized to 1`() {
        val sut = LogarithmicScale(20F, 40F, 2F)

        val actual = sut.normalize(40F)

        assertEquals(1F, actual)
    }

    @Test
    fun `given a scale of base 2 and a range of 20 to 40, 30 is normalized to 0_58`() {
        val sut = LogarithmicScale(20F, 40F, 2F)

        val actual = sut.normalize(30F)

        assertEquals(0.58F, actual, 0.01F)
    }

    @Test
    fun `given a scale of base 2 and a range of 20 to 40, 0 is scaled to 20`() {
        val sut = LogarithmicScale(20F, 40F, 2F)

        val actual = sut.scale(0F)

        assertEquals(20F, actual, 0.01F)
    }

    @Test
    fun `given a scale of base 2 and a range of 20 to 40, 1 is scaled to 40`() {
        val sut = LogarithmicScale(20F, 40F, 2F)

        val actual = sut.scale(1F)

        assertEquals(40F, actual, 0.01F)
    }

    @Test
    fun `given a scale of base 2 and a range of 20 to 40, 0_58 is scaled to 30`() {
        val sut = LogarithmicScale(20F, 40F, 2F)

        // NOTE: A more precise value must be used to get accurate results
        val actual = sut.scale(0.5849F)

        assertEquals(30F, actual, 0.01F)
    }

}