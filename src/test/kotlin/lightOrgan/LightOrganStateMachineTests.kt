package lightOrgan

import input.Input
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LightOrganStateMachineTests {

    private val input: Input = mockk(relaxed = true)
    private val lightOrgan: LightOrgan = mockk(relaxed = true)

    private val newSubscriber: LightOrganSubscriber = mockk()

    private fun createSUT(): LightOrganStateMachine {
        return LightOrganStateMachine(
            input = input,
            lightOrgan = lightOrgan
        )
    }

    @Test
    fun `the state machine is running at initialization if the light organ is already subscribed to the input`() {
        every { input.checkIfSubscribed(lightOrgan) } returns true
        val sut = createSUT()
        assertTrue(sut.isRunning.value)
    }

    @Test
    fun `the state machine is not running at initialization if the light organ is not subscribed to the input`() {
        every { input.checkIfSubscribed(lightOrgan) } returns false
        val sut = createSUT()
        assertFalse(sut.isRunning.value)
    }

    @Test
    fun `the light organ is subscribed to the input when started`() {
        val sut = createSUT()
        sut.start()
        verify { input.addSubscriber(lightOrgan) }
    }

    @Test
    fun `the running state is set to true when the state machine is started`() {
        val sut = createSUT()
        sut.start()
        assertTrue(sut.isRunning.value)
    }

    @Test
    fun `the light organ is unsubscribed to the input when stopped`() {
        val sut = createSUT()
        sut.stop()
        verify { input.removeSubscriber(lightOrgan) }
    }

    @Test
    fun `the running state is set to false when the state machine is stopped`() {
        val sut = createSUT()
        sut.stop()
        assertFalse(sut.isRunning.value)
    }

    @Test
    fun `add a subscriber to the light organ`() {
        val sut = createSUT()
        sut.addSubscriber(newSubscriber)
        verify { lightOrgan.addSubscriber(newSubscriber) }
    }

}
