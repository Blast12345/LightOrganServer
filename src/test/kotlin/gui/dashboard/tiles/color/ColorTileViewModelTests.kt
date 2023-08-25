package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ColorTileViewModelTests {

    private val colorState: MutableState<androidx.compose.ui.graphics.Color> = mockk()
    private val scope = TestScope()

    private val hue = Random.nextFloat()
    private val saturation = Random.nextFloat()
    private val brightness = Random.nextFloat()
    private val color = wrappers.color.Color(hue, saturation, brightness)
    private val composeColor = androidx.compose.ui.graphics.Color.hsv(hue * 360, saturation, brightness)

    private fun createSUT(): ColorTileViewModel {
        return ColorTileViewModel(
            color = colorState,
            scope = scope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `the color state is set when a new color is received`() = runTest {
        val sut = createSUT()

        sut.new(color)
        scope.advanceUntilIdle()

        verify { colorState.value = composeColor }
    }

}