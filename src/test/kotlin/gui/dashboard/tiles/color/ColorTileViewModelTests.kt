package gui.dashboard.tiles.color

import androidx.compose.runtime.MutableState
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextComposeColor

@OptIn(ExperimentalCoroutinesApi::class)
class ColorTileViewModelTests {

    private val colorState: MutableState<androidx.compose.ui.graphics.Color> = mockk()
    private val sutScope = TestScope()

    private val color = nextComposeColor()

    @AfterEach
    fun teardown() {
        sutScope.cancel()
    }

    private fun createSUT(): ColorTileViewModel {
        return ColorTileViewModel(
            color = colorState,
            scope = sutScope
        )
    }

    @Test
    fun `the color state is set when a new color is received`() = runTest {
        val sut = createSUT()

        sut.new(color)
        sutScope.advanceUntilIdle()

        verify { colorState.value = color }
    }

}
