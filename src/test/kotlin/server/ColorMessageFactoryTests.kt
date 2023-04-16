package server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.awt.Color

class ColorMessageFactoryTests {

    private fun createSUT(): ColorMessageFactory {
        return ColorMessageFactory()
    }

    @Test
    fun `get the server message for a color`() {
        val sut = createSUT()
        val color = Color.blue

        val actual = sut.create(color)

        assertEquals("0,0,255", actual)
    }

}