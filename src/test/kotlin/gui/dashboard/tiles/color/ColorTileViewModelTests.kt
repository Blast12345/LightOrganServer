package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import color.ColorProcessor
import gui.tiles.color.ColorTileViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ColorTileViewModelTests {

    private val colorProcessor: ColorProcessor = mockk()
    private val colorState: MutableState<wrappers.color.Color> = mockk()
    private val scope = TestScope()

    private val hue = Random.nextFloat()
    private val saturation = Random.nextFloat()
    private val brightness = Random.nextFloat()
    private val wrappedColor = wrappers.color.Color(hue, saturation, brightness)
    private val composeColor = androidx.compose.ui.graphics.Color.hsv(hue * 360, saturation, brightness)


    private fun createSUT(): ColorTileViewModel {
        return ColorTileViewModel(
            colorProcessor = colorProcessor,
            scope = scope
        )
    }

    @Test
    fun `the default color is black`() {
        val sut = createSUT()

        // TODO: Can we just compare the HSV?
        assertEquals(androidx.compose.ui.graphics.Color.Black, sut.color.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when the color processor has a new color, then update the current color`() = runTest {
        val sut = createSUT()

        colorState.value = wrappedColor
        scope.advanceUntilIdle() // TODO: Is this necessary?

        // TODO: Can we just compare the HSV?
        assertEquals(composeColor, sut.color.value)
    }

}
