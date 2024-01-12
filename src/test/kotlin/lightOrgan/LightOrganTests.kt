package lightOrgan

import color.ColorFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private val newAudio = nextAudioFrame()

    private val subscriber1: LightOrganSubscriber = mockk(relaxed = true)
    private val subscriber2: LightOrganSubscriber = mockk(relaxed = true)
    private val subscribers = mutableSetOf(subscriber1, subscriber2)
    private var colorFactory: ColorFactory = mockk()
    private val nextColor = nextColor()

    @BeforeEach
    fun setupHappyPath() {
        every { colorFactory.create(newAudio) } returns nextColor
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            subscribers = subscribers,
            colorFactory = colorFactory,
        )
    }

    @Test
    fun `send the next color to the subscribers when new audio is received`() {
        val sut = createSUT()

        sut.received(newAudio)

        verify(exactly = 1) { subscriber1.new(nextColor) }
        verify(exactly = 1) { subscriber2.new(nextColor) }
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()
        val newSubscriber: LightOrganSubscriber = mockk()

        sut.addSubscriber(newSubscriber)

        assertTrue(subscribers.contains(newSubscriber))
    }

}
