package extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.awt.Color

class `AwtColor+GetHueTests` {

    @Test
    fun `get the hue for a color`() {
        val color = Color(0, 255, 0)
        Assertions.assertEquals(0.33f, color.getHue(), 0.01f)
    }

}
