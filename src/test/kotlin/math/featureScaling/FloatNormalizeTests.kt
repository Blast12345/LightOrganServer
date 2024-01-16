package math.featureScaling

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FloatNormalizeTests {

    @Test
    fun `1 in a range of 1 to 2 normalizes to 0`() {
        val actual = 1F.normalize(1F, 2F)
        assertEquals(0F, actual)
    }

    @Test
    fun `5 in a range of 3 to 5 normalizes to 1`() {
        val actual = 5F.normalize(3F, 5F)
        assertEquals(1F, actual)
    }

    @Test
    fun `1 in a range of 0 to 2 normalizes to 0_5`() {
        val actual = 1F.normalize(0F, 2F)
        assertEquals(0.5F, actual)
    }

    @Test
    fun `5 in a range of 10 to 20 normalizes to -0_5`() {
        val actual = 5F.normalize(10F, 20F)
        assertEquals(-0.5F, actual)
    }

    @Test
    fun `15 in a range of 5 to 10 normalizes to 2`() {
        val actual = 15F.normalize(5F, 10F)
        assertEquals(2F, actual)
    }

}
