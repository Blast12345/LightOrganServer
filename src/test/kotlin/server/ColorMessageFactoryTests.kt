package server

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ColorMessageFactoryTests {

    private fun createSUT(): ColorMessageFactory {
        return ColorMessageFactory()
    }

    @Test
    fun `get the server message for a color`() {
        val sut = createSUT()
        val color = Color(0, 0, 255)

        val actual = sut.create(color)

        assertEquals("0,0,255", actual)
    }

}
