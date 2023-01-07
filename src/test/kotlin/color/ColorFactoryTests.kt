package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBinListFactoryInterface
import toolkit.getHue
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextFrequencyBins
import java.awt.Color
import kotlin.random.Random

class ColorFactoryTests {

    private var frequencyBinListFactory: FrequencyBinListFactoryInterface = mockk()
    private var hueFactory: HueFactoryInterface = mockk()

    private val audioSignal = nextAudioSignal()
    private val frequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setup() {
        every { frequencyBinListFactory.create(audioSignal) } returns frequencyBins
        every { hueFactory.create(any()) } returns Random.nextFloat()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(
            frequencyBinListFactory = frequencyBinListFactory,
            hueFactory = hueFactory
        )
    }

    @Test
    fun `the colors hue is computed from frequency bins`() {
        val sut = createSUT()
        val hue = Random.nextFloat()
        every { hueFactory.create(frequencyBins) } returns hue
        val actual = sut.create(audioSignal)
        assertEquals(hue, actual.getHue(), 0.001F)
    }

    @Test
    fun `the color is black when there is no hue`() {
        val sut = createSUT()
        every { hueFactory.create(any()) } returns null
        val actual = sut.create(audioSignal)
        assertEquals(Color.black, actual)
    }

}