package gui.dashboard.tiles.color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ColorViewModelFactoryTests {

    private val red = Random.nextFloat()
    private val green = Random.nextFloat()
    private val blue = Random.nextFloat()
    private val awtColor = java.awt.Color(red, green, blue)
    private val composeColor = androidx.compose.ui.graphics.Color(red, green, blue)

    private fun createSUT(): ColorViewModelFactory {
        return ColorViewModelFactory()
    }

    @Test
    fun `the color is converted from a java color`() {
        val sut = createSUT()
        val viewModel = sut.create(awtColor)
        assertEquals(composeColor, viewModel.color)
    }
}