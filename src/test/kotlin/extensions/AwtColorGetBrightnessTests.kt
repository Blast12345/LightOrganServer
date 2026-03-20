package extensions

import color.brightness
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.awt.Color

class AwtColorGetBrightnessTests {

    @Test
    fun `get the brightness for a color`() {
        val color = Color(128, 128, 128)
        assertEquals(0.5f, color.brightness, 0.01f)
    }

}
