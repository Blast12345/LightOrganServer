package lightOrgan

import input.Input
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lightOrgan.color.ColorFactoryInterface
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private var input: Input = mockk()
    private var colorFactory: ColorFactoryInterface = mockk()

    private val receivedAudio = nextAudioFrame()

    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
        every { input.subscribers.add(any()) } returns true
        every { input.subscribers.remove(any()) } returns true
        every { colorFactory.create(any()) } returns nextColor
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            input = input,
            colorFactory = colorFactory
        )
    }

    @Test
    fun `start listening to the input`() {
        val sut = createSUT()
        sut.startListeningToInput()
        verify { input.subscribers.add(sut) }
    }

    @Test
    fun `send the next color to the listeners when new audio is received`() {
        val sut = createSUT()
        val listener: LightOrganListener = mockk(relaxed = true)
        sut.listeners.add(listener)

        sut.received(receivedAudio)

        verify(exactly = 1) { listener.new(nextColor) }
        verify { colorFactory.create(receivedAudio) }
    }

    @Test
    fun `stop listening to the input`() {
        val sut = createSUT()
        sut.stopListeningToInput()
        verify { input.subscribers.remove(sut) }
    }

}