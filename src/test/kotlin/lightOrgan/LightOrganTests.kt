package lightOrgan

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lightOrgan.color.ColorFactoryInterface
import lightOrgan.sound.input.Input
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioSignal
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private var input: Input = mockk()
    private var colorFactory: ColorFactoryInterface = mockk()
    private var listener1: LightOrganListener = mockk()
    private var listener2: LightOrganListener = mockk()

    private val receivedAudio = nextAudioSignal()

    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
        every { input.listeners.add(any()) } returns true
        every { input.listeners.remove(any()) } returns true
        every { colorFactory.create(any()) } returns nextColor
        every { listener1.new(any()) } returns Unit
        every { listener2.new(any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            input = input,
            listeners = mutableSetOf(listener1, listener2),
            colorFactory = colorFactory
        )
    }

    @Test
    fun `start listening to the input`() {
        val sut = createSUT()
        sut.start()
        verify { input.listeners.add(sut) }
    }

    @Test
    fun `send the next color to the listeners when new audio is received`() {
        val sut = createSUT()

        sut.received(receivedAudio)

        verify(exactly = 1) { listener1.new(nextColor) }
        verify(exactly = 1) { listener2.new(nextColor) }
        verify { colorFactory.create(receivedAudio) }
    }

    @Test
    fun `stop listening to the input`() {
        val sut = createSUT()
        sut.start()
        verify { input.listeners.remove(sut) }
    }

}