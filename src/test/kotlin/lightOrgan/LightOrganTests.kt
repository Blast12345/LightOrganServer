package lightOrgan

import audio.samples.AudioFrame
import color.ColorFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.Server
import sound.FrequencyBinsCalculator
import sound.bins.frequency.BassBinsFilter
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextFrequencyBins

class LightOrganTests {

    private val capturedAudio = MutableSharedFlow<AudioFrame>()
    private val frequencyBinsCalculator: FrequencyBinsCalculator = mockk()
    private val frequencyBinsFilter: BassBinsFilter = mockk()
    private val colorFactory: ColorFactory = mockk()
    private val server: Server = mockk()
    private val sutScope = TestScope()

    private val subscriber1: LightOrganSubscriber = mockk(relaxed = true)
    private val subscriber2: LightOrganSubscriber = mockk(relaxed = true)
    private val subscribers = mutableSetOf(subscriber1, subscriber2)

    private val newAudio = nextAudioFrame()
    private val allBins = nextFrequencyBins()
    private val filteredBins = nextFrequencyBins()
    private val newColor = nextColor()

    @BeforeEach
    fun setupHappyPath() {
        every { frequencyBinsCalculator.calculate(newAudio) } returns allBins
        every { frequencyBinsFilter.filter(allBins) } returns filteredBins
        every { colorFactory.create(filteredBins) } returns newColor
        every { subscriber1.new(newColor) } returns Unit
        every { subscriber2.new(newColor) } returns Unit
        every { server.new(newColor) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        sutScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            capturedAudio = capturedAudio,
            frequencyBinsCalculator = frequencyBinsCalculator,
            frequencyBinsFilter = frequencyBinsFilter,
            colorFactory = colorFactory,
            server = server,
            subscribers = subscribers,
            scope = sutScope
        )
    }

    @Test
    fun `when new audio is received, broadcast the color`() = runTest {
        val sut = createSUT()

        capturedAudio.emit(newAudio)

        every { subscriber1.new(newColor) } returns Unit
        every { subscriber2.new(newColor) } returns Unit
        every { server.new(newColor) } returns Unit
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()
        val newSubscriber: LightOrganSubscriber = mockk()

        sut.addSubscriber(newSubscriber)

        assertTrue(subscribers.contains(newSubscriber))
    }

}
