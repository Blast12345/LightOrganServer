package math.featureScaling

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Float+NormalizeLogarithmicallyTests` {

    @Test
    fun `a value of two normalized to a range of 1 to 4 base 2 normalizes to 0_5`() {
        val actual = 2F.normalizeLogarithmically(1F, 4F, 2F)
        assertEquals(0.5F, actual)
    }

    @Test
    fun `a value of 1 normalized to a range of 10 to 100 base 10 normalizes to -1`() {
        val actual = 1F.normalizeLogarithmically(10F, 100F, 10F)
        assertEquals(-1F, actual)
    }

    @Test
    fun `a value of 1000 normalized to a range of 10 to 100 base 10 normalizes to 2`() {
        val actual = 1000F.normalizeLogarithmically(10F, 100F, 10F)
        assertEquals(2F, actual)
    }

    @Test
    fun `a value of 5 normalized to a range of 1 to 10 base 4 normalizes to 0_69`() {
        val actual = 5F.normalizeLogarithmically(1F, 10F, 4F)
        assertEquals(0.69F, actual, 0.01F)
    }
    
}