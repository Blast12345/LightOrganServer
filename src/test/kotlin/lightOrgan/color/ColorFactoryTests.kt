package lightOrgan.color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import lightOrgan.sound.frequencyBins.FrequencyBinsServiceInterface
import lightOrgan.sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactoryInterface
import lightOrgan.sound.frequencyBins.filters.BassFrequencyBinsFilterInterface
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.color.getBrightness
import toolkit.color.getHue
import toolkit.color.getSaturation
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextFrequencyBin
import toolkit.monkeyTest.nextFrequencyBins
import java.awt.Color
import kotlin.random.Random

class ColorFactoryTests {

    private var frequencyBinsService: FrequencyBinsServiceInterface = mockk()
    private val bassFrequencyBinsFilter: BassFrequencyBinsFilterInterface = mockk()
    private var dominantFrequencyBinFactory: DominantFrequencyBinFactoryInterface = mockk()
    private var hueFactory: HueFactoryInterface = mockk()
    private var brightnessFactory: BrightnessFactoryInterface = mockk()

    private val audioSignal = nextAudioSignal()

    private val frequencyBins = nextFrequencyBins()
    private val bassFrequencyBins = nextFrequencyBins()
    private val dominantFrequencyBin = nextFrequencyBin()
    private val hue = Random.nextFloat()
    private val brightness = Random.nextFloat()

    @BeforeEach
    fun setup() {
        every { frequencyBinsService.getFrequencyBins(audioSignal) } returns frequencyBins
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
        val color = sut.create(audioSignal)
        assertEquals(hue, color.getHue(), 0.01F)
    }

    @Test
    fun `the color is fully saturated`() {
        val sut = createSUT()
        val color = sut.create(audioSignal)
        assertEquals(1F, color.getSaturation(), 0.01F)
    }

    @Test
    fun `the colors brightness is determined by the dominant frequency`() {
        val sut = createSUT()
        val color = sut.create(audioSignal)
        assertEquals(brightness, color.getBrightness(), 0.01F)
    }

    @Test
    fun `return black when a dominant frequency cannot be determined`() {
        val sut = createSUT()
        every { dominantFrequencyBinFactory.create(any()) } returns null
        val color = sut.create(audioSignal)
        assertEquals(Color.black, color)
    }


}