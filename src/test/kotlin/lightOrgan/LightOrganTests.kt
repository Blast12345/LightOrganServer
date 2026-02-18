package lightOrgan

import color.ColorFactory
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextColor

class LightOrganTests {

    private val subscriber1: LightOrganSubscriber = mockk(relaxed = true)
    private val subscriber2: LightOrganSubscriber = mockk(relaxed = true)
    private val subscribers = mutableSetOf(subscriber1, subscriber2)
    private var colorFactory: ColorFactory = mockk()
    private val nextColor = nextColor()

    @BeforeEach
    fun setupHappyPath() {
//        every { colorFactory.create(newAudio) } returns nextColor
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): LightOrgan {
        return LightOrgan(
            subscribers = subscribers,
            colorFactory = colorFactory,
        )
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()
        val newSubscriber: LightOrganSubscriber = mockk()

        sut.addSubscriber(newSubscriber)

        assertTrue(subscribers.contains(newSubscriber))
    }

}
