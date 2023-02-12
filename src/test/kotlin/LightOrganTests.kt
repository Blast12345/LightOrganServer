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
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private var config: Config = mockk()

    //    private var input: InputInterface = mockk()
    private var audioCache: AudioCacheInterface = mockk()
    private var colorBroadcaster: ColorBroadcaster = mockk()
    private var colorFactory: ColorFactoryInterface = mockk()

    private val receivedAudio = nextAudioSignal()

    private val cachedAudio = nextAudioSignal()
    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
//        every { input.listenForAudio(any()) } returns Unit
        every { audioCache.setAudio(any()) } returns Unit
        every { audioCache.getAudio() } returns cachedAudio
        every { audioCache.clear() } returns Unit
//        every { colorBroadcaster.startBroadcasting(any()) } returns Unit
        every { colorFactory.create(any()) } returns nextColor
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
//            config = config,
//            input = input,
            audioCache = audioCache,
            colorBroadcaster = colorBroadcaster,
            colorFactory = colorFactory
        )
    }

    // TODO:
//    @Test
//    fun `start listening for audio at initialization`() {
//        val sut = createSUT()
//        verify { input.listenForAudio(sut) }
//    }

    @Test
    fun `the cache is updated when new audio is received`() {
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