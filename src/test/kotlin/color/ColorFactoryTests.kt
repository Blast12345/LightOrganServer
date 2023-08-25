package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBinsService
import sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactory
import sound.frequencyBins.filters.BassFrequencyBinsFilter
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextFrequencyBin
import toolkit.monkeyTest.nextFrequencyBins
import wrappers.color.Color
import kotlin.random.Random

class ColorFactoryTests {

    private var frequencyBinsService: FrequencyBinsService = mockk()
    private val bassFrequencyBinsFilter: BassFrequencyBinsFilter = mockk()
    private var dominantFrequencyBinFactory: DominantFrequencyBinFactory = mockk()
    private var hueFactory: HueFactory = mockk()
    private var brightnessFactory: BrightnessFactory = mockk()

    private val audioFrame = nextAudioFrame()

    private val frequencyBins = nextFrequencyBins()
    private val bassFrequencyBins = nextFrequencyBins()
    private val dominantFrequencyBin = nextFrequencyBin()
    private val hue = Random.nextFloat()
    private val brightness = Random.nextFloat()

    @BeforeEach
    fun setup() {
        every { frequencyBinsService.getFrequencyBins(audioFrame) } returns frequencyBins
        every { bassFrequencyBinsFilter.filter(frequencyBins) } returns bassFrequencyBins
        every { dominantFrequencyBinFactory.create(bassFrequencyBins) } returns dominantFrequencyBin
        every { hueFactory.create(dominantFrequencyBin) } returns hue
        every { brightnessFactory.create(dominantFrequencyBin) } returns brightness
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(
            frequencyBinsService = frequencyBinsService,
            bassFrequencyBinsFilter = bassFrequencyBinsFilter,
            dominantFrequencyBinFactory = dominantFrequencyBinFactory,
            hueFactory = hueFactory,
            brightnessFactory = brightnessFactory
        )
    }

    @Test
    fun `the colors hue is determined by the dominant frequency`() {
        val sut = createSUT()
        val color = sut.create(audioFrame)
        assertEquals(hue, color.hue, 0.01F)
    }

    @Test
    fun `the color is fully saturated`() {
        val sut = createSUT()
        val color = sut.create(audioFrame)
        assertEquals(1F, color.saturation, 0.01F)
    }

    @Test
    fun `the colors brightness is determined by the dominant frequency`() {
        val sut = createSUT()
        val color = sut.create(audioFrame)
        assertEquals(brightness, color.brightness, 0.01F)
    }

    @Test
    fun `return black when a dominant frequency cannot be determined`() {
        val sut = createSUT()
        every { dominantFrequencyBinFactory.create(any()) } returns null

        val color = sut.create(audioFrame)

        val black = Color(0, 0, 0)
        assertEquals(black, color)
    }


}