package math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NextPowerOfTwoTests {

    @Test
    fun `value that is already a power of two is unchanged`() {
        assertEquals(1, nextPowerOfTwo(1))
        assertEquals(16, nextPowerOfTwo(16))
    }

    @Test
    fun `rounds up to the next power of two`() {
        assertEquals(16, nextPowerOfTwo(9))
        assertEquals(16, nextPowerOfTwo(15))
    }

    @Test
    fun `throw when the value is 0`() {
        assertThrows<IllegalArgumentException> {
            nextPowerOfTwo(0)
        }
    }

    @Test
    fun `throw when the value is negative`() {
        assertThrows<IllegalArgumentException> {
            nextPowerOfTwo(-5)
        }
    }
    
}