package gui.dashboard.tiles.color

import io.mockk.clearAllMocks
import lightOrgan.color.ColorManagerFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextColor

class ColorTileViewModelTests {

    private lateinit var colorManager: ColorManagerFixture

    private val color1 = nextColor()
    private val color2 = nextColor()


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
    fun `when a new color is available, then show that color`() {
        val sut = createSUT()

        colorManager.colorFlow.value = color1
        assertEquals(color1, sut.color.value)

        colorManager.colorFlow.value = color2
        assertEquals(color2, sut.color.value)
    }

}
