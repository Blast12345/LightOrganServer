package gui.dashboard.snackbar

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import toolkit.extensions.collectInto
import toolkit.monkeyTest.nextString
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlowSnackbarControllerTests {

    val message1 = nextString("1")
    val message2 = nextString("2")

    @Test
    fun `show a message`() = runTest {
        val sut = FlowSnackbarController()
        val received = sut.messages.collectInto(this)

        sut.show(message1)

        runCurrent()
        assertEquals(1, received.count())
        assertEquals(message1, received[0])
    }

    @Test
    fun `messages shown before the subscriber is ready are queued`() = runTest {
        val sut = FlowSnackbarController()

        sut.show(message1)
        sut.show(message2)

        val received = sut.messages.collectInto(this)

        runCurrent()
        assertEquals(2, received.count())
        assertEquals(message1, received[0])
        assertEquals(message2, received[1])
    }

}