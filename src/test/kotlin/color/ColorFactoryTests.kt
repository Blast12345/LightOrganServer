package color

import color.dominantFrequency.DominantFrequencyBinFinderInterface
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBinsServiceInterface
import toolkit.color.getBrightness
import toolkit.color.getHue
import toolkit.color.getSaturation
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextFrequencyBin
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class ColorFactoryTests {

    private var frequencyBinsService: FrequencyBinsServiceInterface = mockk()
    private var dominantFrequencyBinFinder: DominantFrequencyBinFinderInterface = mockk()
    private var hueFactory: HueFactoryInterface = mockk()
    private var brightnessFactory: BrightnessFactoryInterface = mockk()

    private val audioSignal = nextAudioSignal()

    private val frequencyBins = nextFrequencyBins()
    private val dominantFrequencyBin = nextFrequencyBin()
    private val hue = Random.nextFloat()
    private val brightness = Random.nextFloat()

    @BeforeEach
    fun setup() {
        every { frequencyBinsService.getFrequencyBins(any()) } returns frequencyBins
        every { dominantFrequencyBinFinder.find(any()) } returns dominantFrequencyBin
        every { hueFactory.create(any()) } returns hue
        every { brightnessFactory.create(any()) } returns brightness
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(
            frequencyBinsService = frequencyBinsService,
            dominantFrequencyBinFinder = dominantFrequencyBinFinder,
            hueFactory = hueFactory,
            brightnessFactory = brightnessFactory
        )
    }

    @Test
    fun `the colors hue is determined by sound`() {
        val sut = createSUT()
        val color = sut.create(audioSignal)
        assertEquals(hue, color.getHue(), 0.01F)
        verify { hueFactory.create(frequencyBins) } // TODO: Expand me
    }

    @Test
    fun `the color is fully saturated`() {
        val sut = createSUT()
        val color = sut.create(audioSignal)
        assertEquals(1F, color.getSaturation(), 0.01F)
    }

    @Test
    fun `the colors brightness is determined by sound`() {
        val sut = createSUT()
        val color = sut.create(audioSignal)
        assertEquals(brightness, color.getBrightness(), 0.01F)
        verify { brightnessFactory.create(dominantFrequencyBin) }
    }

}