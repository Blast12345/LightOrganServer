package math.perception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StevensPowerLawTests {

    @Test
    fun `given the intensity of a stimulus, get the perceived intensity`() {
        val perceivedIntensity = StevensPowerLaw.BRIGHTNESS_BRIEF_FLASH.perceivedIntensity(4.0)

        assertEquals(2.0, perceivedIntensity)
    }

    @Test
    fun `given a perceived intensity, get the intensity of stimulus`() {
        val stimulusIntensity = StevensPowerLaw.BRIGHTNESS_BRIEF_FLASH.stimulusIntensity(2.0)

        assertEquals(4.0, stimulusIntensity)
    }

}