import color.ColorFactoryInterface
import colorBroadcaster.ColorBroadcaster
import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.input.InputInterface
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextColor
import java.awt.Color

class LightOrganTests {

    private var config: Config = mockk()
    private var input: InputInterface = mockk()
    private var audioCache: AudioCacheInterface = mockk()
    private var colorBroadcaster: ColorBroadcaster = mockk()
    private var colorFactory: ColorFactoryInterface = mockk()

    private val receivedAudio = nextAudioSignal()

    private val cachedAudio = nextAudioSignal()
    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
        every { input.listenForAudio(any()) } returns Unit
        every { audioCache.setAudio(any()) } returns Unit
        every { audioCache.getAudio() } returns cachedAudio
        every { audioCache.clear() } returns Unit
//        every { colorBroadcaster.startBroadcasting(any()) } returns Unit
        every { colorFactory.create(any()) } returns nextColor
//        every { server.sendColor(any()) } returns Unit
//        every { systemTime.currentTimeInSeconds() } returns Random.nextDouble()
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

    @Test
    fun `start listening for audio at initialization`() {
        val sut = createSUT()
        verify { input.listenForAudio(sut) }
    }

    @Test
    fun `the cache is updated when new audio is received`() {
        val sut = createSUT()
        sut.receivedAudio(receivedAudio)
        verify { audioCache.setAudio(receivedAudio) }
    }

//    @Test
//    fun `start broadcasting colors at initialization`() {
//        val sut = createSUT()
//        verify { colorBroadcaster.startBroadcasting(sut) }
//    }

//    @Test
//    fun `not ready for next color when audio cache has no audio`() {
//        val sut = createSUT()
//        every { audioCache.getAudio() } returns null
//
//        val actual = sut.isReadyForNextColor()
//
//        assertFalse(actual)
//    }
//
//    @Test
//    fun `ready for next color when audio cache is has audio`() {
//        val sut = createSUT()
//        val actual = sut.isReadyForNextColor()
//        assertTrue(actual)
//    }

    @Test
    fun `the next color is created from the cached audio`() {
        val sut = createSUT()

        val actual = sut.getNextColor()

        assertEquals(nextColor, actual)
        verify { colorFactory.create(cachedAudio) }
    }

    @Test
    fun `the next color black when there is no cached audio`() {
        val sut = createSUT()
        every { audioCache.getAudio() } returns null

        val actual = sut.getNextColor()

        assertEquals(Color.black, actual)
    }

    @Test
    fun `the cache is cleared when the next color is created`() {
        val sut = createSUT()
        sut.getNextColor()
        verify { audioCache.clear() }
    }

}