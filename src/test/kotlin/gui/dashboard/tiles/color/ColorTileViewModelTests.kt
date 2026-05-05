package gui.dashboard.tiles.color

import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import lightOrgan.color.ColorManagerFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextComposeColor

class ColorTileViewModelTests {

    private lateinit var colorManager: ColorManagerFixture

    private val color1 = nextComposeColor()
    private val color2 = nextComposeColor()

    @BeforeEach
    fun setupHappyPath() {
        colorManager = ColorManagerFixture.create()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorTileViewModel {
        return ColorTileViewModel(
            colorManager = colorManager.mock
        )
    }

    @Test
    fun `when a new color is available, then show that color`() = runTest {
        val sut = createSUT()

        colorManager.colorFlow.emit(color1)
        assertEquals(color1, sut.color.value)

        colorManager.colorFlow.emit(color2)
        assertEquals(color2, sut.color.value)
    }

}
