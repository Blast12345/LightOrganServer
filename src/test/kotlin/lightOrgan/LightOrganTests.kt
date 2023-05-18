package lightOrgan

import color.ColorFactory
import input.Input
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private var input: Input = mockk()
    private var colorFactory: ColorFactory = mockk()
    private val subscriber1: LightOrganSubscriber = mockk(relaxed = true)
    private val subscriber2: LightOrganSubscriber = mockk(relaxed = true)

    private val nextColor = nextColor()

    @BeforeEach
    fun setup() {
        every { input.addSubscriber(any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            input = input,
            colorFactory = colorFactory,
            subscribers = mutableSetOf(subscriber1, subscriber2)
        )
    }

    @Test
    fun `the light organ begins listening to the input upon initialization`() {
        val sut = createSUT()
        verify { input.addSubscriber(sut) }
    }

    @Test
    fun `send the next color to the subscribers when new audio is received`() {
        val sut = createSUT()
        every { colorFactory.create(any()) } returns nextColor

        val receivedAudio = nextAudioFrame()
        sut.received(receivedAudio)

        verify { colorFactory.create(receivedAudio) }
        verify(exactly = 1) { subscriber1.new(nextColor) }
        verify(exactly = 1) { subscriber2.new(nextColor) }
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is`() {
        val sut = createSUT()

        val actual = sut.checkIfSubscribed(subscriber1)

        assertTrue(actual)
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is not`() {
        val sut = createSUT()

        val newSubscriber: LightOrganSubscriber = mockk()
        val actual = sut.checkIfSubscribed(newSubscriber)

        assertFalse(actual)
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()

        val newSubscriber: LightOrganSubscriber = mockk()
        sut.addSubscriber(newSubscriber)

        assertTrue(sut.checkIfSubscribed(newSubscriber))
    }

}