package dsp.filtering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextPositiveInt

class FilterOrderTests {

    // plain order
    @Test
    fun `create a filter order`() {
        val order = nextPositiveInt()

        val sut = FilterOrder(order)

        assertEquals(order, sut.value)
    }

    @Test
    fun `orders must be greater than or equal to 1`() {
        assertThrows<Exception> { FilterOrder(-1) }
        assertThrows<Exception> { FilterOrder(0) }
    }

    // Order from slope
    @Test
    fun `create a filter order from a slope`() {
        val order = nextPositiveInt()
        val slope = order * 6

        val sut = FilterOrder.fromDbPerOctave(slope)

        assertEquals(order, sut.value)
    }

    @Test
    fun `slope must be a multiple of 6`() {
        assertThrows<Exception> { FilterOrder.fromDbPerOctave(5) }
    }

    @Test
    fun `slope must be non-zero`() {
        assertThrows<Exception> { FilterOrder.fromDbPerOctave(0) }
    }

}