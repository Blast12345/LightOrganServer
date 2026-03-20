package extensions

import color.hue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.awt.Color

class AwtColorGetHueTests {

    @Test
    fun `get the hue for a color`() {
        val color = Color(0, 255, 0)
        Assertions.assertEquals(0.33f, color.hue, 0.01f)
    }

}
