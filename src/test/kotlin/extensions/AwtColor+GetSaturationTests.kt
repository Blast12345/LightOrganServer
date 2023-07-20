package extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.awt.Color

class `AwtColor+GetSaturationTests` {

    @Test
    fun `get the saturation for a color`() {
        val color = Color(64, 128, 64)
        Assertions.assertEquals(0.5f, color.getSaturation(), 0.01f)
    }

}