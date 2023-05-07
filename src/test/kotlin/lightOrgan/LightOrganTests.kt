package lightOrgan

import color.ColorFactory
import input.Input
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private var input: Input = mockk()
    private var colorFactory: ColorFactory = mockk()

    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
        every { input.subscribers.add(any()) } returns true
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
    fun `the light organ begins listening to the input upon initialization`() {
        val sut = createSUT()
        verify { input.subscribers.add(sut) }
    }

    @Test
    fun `send the next color to the subscribers when new audio is received`() {
        val sut = createSUT()
        val listener: LightOrganSubscriber = mockk(relaxed = true)
        sut.subscribers.add(listener)

        val receivedAudio = nextAudioFrame()
        sut.received(receivedAudio)

        verify(exactly = 1) { listener.new(nextColor) }
        verify { colorFactory.create(receivedAudio) }
    }

}