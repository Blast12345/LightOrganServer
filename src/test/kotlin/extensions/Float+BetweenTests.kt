package extensions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Float+BetweenTests` {

    @Test
    fun `return the minimum when the number is less than the minimum`() {
        val number = 1F

        val actual = number.between(2F, 3F)

        assertEquals(2F, actual)
    }

    @Test
    fun `return the maximum when the number is greater than the maximum`() {
        val number = 6F

        val actual = number.between(3F, 5F)

        assertEquals(5F, actual)
    }

    @Test
    fun `return the number is between the minimum and maximum`() {
        val number = 3F

        val actual = number.between(1F, 5F)

        assertEquals(3F, actual)
    }


}
