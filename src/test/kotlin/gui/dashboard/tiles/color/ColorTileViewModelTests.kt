package gui.dashboard.tiles.color

import io.mockk.clearAllMocks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lightOrgan.color.ColorManagerFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.extensions.collectInto
import toolkit.monkeyTest.nextColor

@OptIn(ExperimentalCoroutinesApi::class)
class ColorTileViewModelTests {

    private lateinit var colorManager: ColorManagerFixture
    private val sutScope = TestScope()

    private val color1 = nextColor()
    private val color2 = nextColor()

    @BeforeEach
    fun setupHappyPath() {
        colorManager = ColorManagerFixture.create()
    }

    @AfterEach
    fun teardown() {
        sutScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): ColorTileViewModel {
        return ColorTileViewModel(
            colorManager = colorManager.mock
        )
    }

    @Test
    fun `when the color is available, then display the color`() = runTest {
        val sut = createSUT()
        val received = colorManager.color.collectInto(sutScope)

        colorManager.color.value = color1
        sutScope.advanceUntilIdle()

        colorManager.color.value = color2
        sutScope.advanceUntilIdle()

        assertEquals(color1, received[0])
        assertEquals(color2, received[1])
    }

}
