package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ColorViewModelTests {

    private val colorState: MutableState<androidx.compose.ui.graphics.Color> = mockk()

    private val red = Random.nextFloat()
    private val green = Random.nextFloat()
    private val blue = Random.nextFloat()
    private val awtColor = java.awt.Color(red, green, blue)
    private val composeColor = androidx.compose.ui.graphics.Color(red, green, blue)

    private fun createSUT(): ColorViewModel {
        return ColorViewModel(
            color = colorState
        )
    }

    @Test
    fun `the color state is set when a new color is received`() {
        val sut = createSUT()
        sut.new(awtColor)
        verify { colorState.value = composeColor }
    }

}