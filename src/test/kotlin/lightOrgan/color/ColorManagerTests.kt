package lightOrgan.color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lightOrgan.color.calculator.ColorCalculator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.extensions.collectInto
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextFrequencyBins
import wrappers.color.Color

@OptIn(ExperimentalCoroutinesApi::class)
class ColorManagerTests {

    private val colorCalculator: ColorCalculator = mockk()

    private val frequencyBins = nextFrequencyBins()
    private val color = nextColor()

    private val collectionScope = TestScope()

    @BeforeEach
    fun setupHappyPath() {
        every { colorCalculator.calculate(frequencyBins) } returns color
    }

    @AfterEach
    fun tearDown() {
        collectionScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): ColorManager {
        return ColorManager(
            colorCalculator = colorCalculator
        )
    }

    @Test
    fun `calculate a color from frequency bins`() {
        val sut = createSUT()

        val actual = sut.calculate(frequencyBins)

        assertEquals(color, actual)
    }

    @Test
    fun `when the color could not be calculated, return black`() {
        val sut = createSUT()
        every { colorCalculator.calculate(frequencyBins) } returns null

        val actual = sut.calculate(frequencyBins)

        assertEquals(Color.black, actual)
    }

    @Test
    fun `the color is emitted after calculation`() = runTest {
        val sut = createSUT()
        val received = sut.color.collectInto(collectionScope)

        sut.calculate(frequencyBins)
        collectionScope.advanceUntilIdle()

        assertEquals(listOf(color), received)
    }

}