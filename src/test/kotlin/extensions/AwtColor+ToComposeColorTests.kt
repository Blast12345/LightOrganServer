package extensions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.awt.Color

class `AwtColor+ToComposeColorTests` {

    @Test
    fun `convert the awt color to a compose color`() {
        val awtColor = Color.cyan
        val composeColor = awtColor.toComposeColor()
        assertEquals(0f, composeColor.red, 0.01f)
        assertEquals(1f, composeColor.green, 0.01f)
        assertEquals(1f, composeColor.blue, 0.01f)
    }

}