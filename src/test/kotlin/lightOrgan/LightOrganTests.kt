package lightOrgan

import color.ColorFactory
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

    private var colorFactory: ColorFactory = mockk()

    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
        every { colorFactory.create(any()) } returns nextColor
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            colorFactory = colorFactory
        )
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