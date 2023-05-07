package gui.dashboard.tiles.color

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ColorViewModelFactoryTests {

    private fun createSUT(): ColorViewModelFactory {
        return ColorViewModelFactory()
    }

    @Test
    fun `the color defaults to black`() {
        val sut = createSUT()
        val viewModel = sut.create()
        assertEquals(Color.Black, viewModel.color.value)
    }
}