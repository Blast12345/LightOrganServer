package extensions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.awt.Color

class `AwtColor+GetBrightnessTests` {

    @Test
    fun `get the brightness for a color`() {
        val color = Color(128, 128, 128)
        assertEquals(0.5f, color.getBrightness(), 0.01f)
    }

}
