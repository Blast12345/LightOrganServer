import color.ColorFactoryInterface
import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.ServerInterface
import sound.input.Input
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private var config: Config = mockk()
    private var input: Input = mockk()
    private var colorFactory: ColorFactoryInterface = mockk()
    private var server: ServerInterface = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private var colorScope = TestScope()

    private val receivedAudio = nextAudioSignal()

    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
        every { input.setDelegate(any()) } returns Unit
        every { colorFactory.create(any()) } returns nextColor
        every { server.sendColor(any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createSUT(): LightOrgan {
        return LightOrgan(
            config = config,
            input = input,
            colorFactory = colorFactory,
            server = server,
            colorScope = colorScope
        )
    }

    @Test
    fun `begin listening to the input upon initialization`() {
        val sut = createSUT()
        verify { input.setDelegate(sut) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `send the next color to the server when new audio is received`() = colorScope.runTest {
        val sut = createSUT()
        sut.received(receivedAudio)
        advanceUntilIdle()
        verify { server.sendColor(nextColor) }
        verify { colorFactory.create(receivedAudio) }
    }

}