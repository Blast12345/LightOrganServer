package dsp.filtering.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextEnum
import toolkit.monkeyTest.nextPositiveInt

class FilterConfigTests {

    private val family = nextEnum<FilterFamily>()
    private val frequency = nextPositiveInt().toFloat()
    private val order = nextPositiveInt()
    private val dbPerOctave = 6 * nextPositiveInt()

    // Low pass
    @Test
    fun `create low pass config`() {
        val config = FilterConfig.lowPass(family, frequency, order)

        assertEquals(family, config.family)
        assertEquals(FilterTopology.LowPass(frequency), config.topology)
        assertEquals(order, config.order)
    }

    @Test
    fun `create low pass from slope`() {
        val config = FilterConfig.lowPassFromSlope(family, frequency, dbPerOctave)

        assertEquals(family, config.family)
        assertEquals(FilterTopology.LowPass(frequency), config.topology)
        assertEquals(dbPerOctave / 6, config.order)
    }

    // High pass
    @Test
    fun `create high pass config`() {
        val config = FilterConfig.highPass(family, frequency, order)

        assertEquals(family, config.family)
        assertEquals(FilterTopology.HighPass(frequency), config.topology)
        assertEquals(order, config.order)
    }

    @Test
    fun `create high pass from slope`() {
        val config = FilterConfig.highPassFromSlope(family, frequency, dbPerOctave)

        assertEquals(family, config.family)
        assertEquals(FilterTopology.HighPass(frequency), config.topology)
        assertEquals(dbPerOctave / 6, config.order)
    }

    // Slope validation
    @Test
    fun `slope must be a multiple of 6`() {
        assertThrows<IllegalArgumentException> {
            FilterConfig.lowPassFromSlope(family, frequency, dbPerOctave = 10)
        }

        assertThrows<IllegalArgumentException> {
            FilterConfig.highPassFromSlope(family, frequency, dbPerOctave = 10)
        }
    }

    @Test
    fun `slope must be positive`() {
        assertThrows<IllegalArgumentException> {
            FilterConfig.lowPassFromSlope(family, frequency, dbPerOctave = 0)
        }

        assertThrows<IllegalArgumentException> {
            FilterConfig.highPassFromSlope(family, frequency, dbPerOctave = 0)
        }
    }

}