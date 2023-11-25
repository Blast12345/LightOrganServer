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
import sound.frequencyBins.dominant.DominantBassFrequencyBinCalculator
import sound.frequencyBins.listCalculator.FrequencyBinListCalculator
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextFrequencyBin
import toolkit.monkeyTest.nextFrequencyBinList

class LightOrganTests {

    private val subscriber1: LightOrganSubscriber = mockk(relaxed = true)
    private val subscriber2: LightOrganSubscriber = mockk(relaxed = true)
    private var colorFactory: ColorFactory = mockk()
    private val nextColor = nextColor()
    private val frequencyBinListCalculator: FrequencyBinListCalculator = mockk()
    private val frequencyBinList = nextFrequencyBinList()
    private var dominantBassFrequencyBinCalculator: DominantBassFrequencyBinCalculator = mockk()
    private val dominantBassFrequencyBin = nextFrequencyBin()

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            subscribers = mutableSetOf(subscriber1, subscriber2),
            colorFactory = colorFactory,
            frequencyBinListCalculator = frequencyBinListCalculator,
            dominantBassFrequencyBinCalculator = dominantBassFrequencyBinCalculator
        )
    }

    @Test
    fun `send the next color to the subscribers when new audio is received`() {
        val sut = createSUT()
        val receivedAudio = nextAudioFrame()
        every { frequencyBinListCalculator.calculate(receivedAudio.samples, receivedAudio.format) } returns frequencyBinList
        every { dominantBassFrequencyBinCalculator.calculate(frequencyBinList) } returns dominantBassFrequencyBin
        every { colorFactory.create(dominantBassFrequencyBin) } returns nextColor

        sut.received(receivedAudio)

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
        val newSubscriber: LightOrganSubscriber = mockk()

        val actual = sut.checkIfSubscribed(newSubscriber)

        assertFalse(actual)
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()
        val newSubscriber: LightOrganSubscriber = mockk()

        sut.addSubscriber(newSubscriber)

        assertTrue(sut.checkIfSubscribed(newSubscriber))
    }

}