package extensions

import color.saturation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.awt.Color

class AwtColorGetSaturationTests {

    @Test
    fun `get the saturation for a color`() {
        val color = Color(64, 128, 64)
        Assertions.assertEquals(0.5f, color.saturation, 0.01f)
    }

}
