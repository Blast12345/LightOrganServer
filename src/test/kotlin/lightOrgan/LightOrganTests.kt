package lightOrgan

import color.ColorFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBinsService
import sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactory
import sound.frequencyBins.filters.BassFrequencyBinsFilter
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextFrequencyBin
import toolkit.monkeyTest.nextFrequencyBins

class LightOrganTests {

    private val subscriber1: LightOrganSubscriber = mockk(relaxed = true)
    private val subscriber2: LightOrganSubscriber = mockk(relaxed = true)
    private var colorFactory: ColorFactory = mockk()
    private val nextColor = nextColor()
    private var frequencyBinsService: FrequencyBinsService = mockk()
    private val frequencyBins = nextFrequencyBins()
    private val bassFrequencyBinsFilter: BassFrequencyBinsFilter = mockk()
    private val bassFrequencyBins = nextFrequencyBins()
    private var dominantFrequencyBinFactory: DominantFrequencyBinFactory = mockk()
    private val dominantFrequencyBin = nextFrequencyBin()

    private val audioFrame = nextAudioFrame()
    private val newSubscriber: LightOrganSubscriber = mockk()

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            subscribers = mutableSetOf(subscriber1, subscriber2),
            colorFactory = colorFactory,
            frequencyBinsService = frequencyBinsService,
            bassFrequencyBinsFilter = bassFrequencyBinsFilter,
            dominantFrequencyBinFactory = dominantFrequencyBinFactory
        )
    }

    @Test
    fun `send the next color to the subscribers when new audio is received`() {
        val sut = createSUT()
        every { frequencyBinsService.getFrequencyBins(audioFrame) } returns frequencyBins
        every { bassFrequencyBinsFilter.filter(frequencyBins) } returns bassFrequencyBins
        every { dominantFrequencyBinFactory.create(bassFrequencyBins) } returns dominantFrequencyBin
        every { colorFactory.create(dominantFrequencyBin) } returns nextColor

        sut.received(audioFrame)

        verify(exactly = 1) { subscriber1.new(nextColor) }
        verify(exactly = 1) { subscriber2.new(nextColor) }
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is`() {
        val sut = createSUT()
        val actual = sut.checkIfSubscribed(subscriber1)
        assertTrue(actual)
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is not`() {
        val sut = createSUT()
        val actual = sut.checkIfSubscribed(newSubscriber)
        assertFalse(actual)
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()
        sut.addSubscriber(newSubscriber)
        assertTrue(sut.checkIfSubscribed(newSubscriber))
    }

}