package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ColorViewModelTests {

    private val colorState: MutableState<androidx.compose.ui.graphics.Color> = mockk()
    private val scope = TestScope()

    private val red = Random.nextFloat()
    private val green = Random.nextFloat()
    private val blue = Random.nextFloat()
    private val awtColor = java.awt.Color(red, green, blue)
    private val composeColor = androidx.compose.ui.graphics.Color(red, green, blue)

    private fun createSUT(): ColorViewModel {
        return ColorViewModel(
            color = colorState,
            scope = scope
        )
    }

    @Test
    fun `the color state is set when a new color is received`() = runTest {
        val sut = createSUT()

        sut.new(awtColor)
        scope.advanceUntilIdle()

        verify { colorState.value = composeColor }
    }

}