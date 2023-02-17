import color.ColorFactoryInterface
import colorBroadcaster.ColorBroadcaster
import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.input.Input
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private var config: Config = mockk()

    // TODO: It is a little smelly that I'm not doing anything with this
    // But the principle I was following is that class should need to be "started" after initialization.
    // Anything remotely like a start function (e.g. beginListeningWith(delegate) ) creates significant test smells.
    // maybe init with delegate as null and have a setDelegate function; test can use the intializer version and prod code can use the set
    private var input: Input = mockk()
    private var audioCache: AudioCacheInterface = mockk()
    private var colorBroadcaster: ColorBroadcaster = mockk() // TODO: Or this.
    private var colorFactory: ColorFactoryInterface = mockk()

    private val receivedAudio = nextAudioSignal()

    private val cachedAudio = nextAudioSignal()
    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
        every { audioCache.setAudio(any()) } returns Unit
        every { audioCache.getAudio() } returns cachedAudio
        every { audioCache.clear() } returns Unit
        every { colorFactory.create(any()) } returns nextColor
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            input = input,
            audioCache = audioCache,
            colorBroadcaster = colorBroadcaster,
            colorFactory = colorFactory
        )
    }

    @Test
    fun `the audio cache is updated when new audio is received`() {
        val sut = createSUT()
        sut.received(receivedAudio)
        verify { audioCache.setAudio(receivedAudio) }
    }

    @Test
    fun `the next color is created from the cached audio`() {
        val sut = createSUT()

        val actual = sut.getNextColor()

        assertEquals(nextColor, actual)
        verify { colorFactory.create(cachedAudio) }
    }

    @Test
    fun `the next color null when there is no cached audio`() {
        val sut = createSUT()
        every { audioCache.getAudio() } returns null

        val actual = sut.getNextColor()

        assertNull(actual)
    }

    @Test
    fun `the audio cache is cleared after audio is retrieved`() {
        val sut = createSUT()
        sut.getNextColor()
        verify { audioCache.clear() }
    }

}