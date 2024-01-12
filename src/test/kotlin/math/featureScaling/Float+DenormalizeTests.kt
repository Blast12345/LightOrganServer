package math.featureScaling

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Float+DenormalizeTests` {

    @Test
    fun `given a range of 10 to 20, -1_5 denormalizes to -5`() {
        val number = -1.5F

        val actual = number.denormalize(10F, 20F)

        assertEquals(-5F, actual)
    }


    @Test
    fun `given a range of 10 to 20, 0 denormalizes to 10`() {
        val number = 0F

        val actual = number.denormalize(10F, 20F)

        assertEquals(10F, actual)
    }


    @Test
    fun `given a range of 10 to 20, 0_75 denormalizes to 17_5`() {
        val number = 0.75F

        val actual = number.denormalize(10F, 20F)

        assertEquals(17.5F, actual)
    }


    @Test
    fun `given a range of 10 to 20, 1 denormalizes to 20`() {
        val number = 1F

        val actual = number.denormalize(10F, 20F)

        assertEquals(20F, actual)
    }

    @Test
    fun `given a range of 10 to 20, 1_5 denormalizes to 25`() {
        val number = 1.5F

        val actual = number.denormalize(10F, 20F)

        assertEquals(25F, actual)
    }

}
