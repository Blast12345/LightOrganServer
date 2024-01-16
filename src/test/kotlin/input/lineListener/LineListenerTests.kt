package input.lineListener

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextByteArray
import toolkit.monkeyTest.nextConfig
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine
import kotlin.random.Random

class LineListenerTests {

    private val scope = TestScope()

    private val dataLine: TargetDataLine = mockk(relaxed = true)
    private val targetDataLineReader: TargetDataLineReader = mockk()
    private val subscriber1: LineListenerSubscriber = mockk(relaxed = true)
    private val subscriber2: LineListenerSubscriber = mockk(relaxed = true)
    private val config = nextConfig()

    private val newSamples = nextByteArray()
    private val format: AudioFormat = mockk()

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): LineListener {
        return LineListener(
            dataLine = dataLine,
            scope = scope,
            targetDataLineReader = targetDataLineReader,
            subscribers = mutableSetOf(subscriber1, subscriber2),
            config = config
        )
    }

    @Test
    fun `the data line is prepared to be read upon initialization`() {
        createSUT()

        verifyOrder {
            dataLine.open()
            dataLine.start()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `the data line is checked for new data every X milliseconds`() = scope.runTest {
        createSUT()
        every { targetDataLineReader.getAvailableData(dataLine) } returns newSamples

        val iterations = Random.nextInt(1, 8)
        val durationOfTwoLoops = iterations * config.millisecondsToWaitBetweenCheckingForNewAudio
        advanceTimeBy(durationOfTwoLoops)

        coVerify(exactly = iterations) { targetDataLineReader.getAvailableData(dataLine) }

        scope.coroutineContext.cancelChildren()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `samples are given to the subscribers`() = scope.runTest {
        createSUT()
        every { targetDataLineReader.getAvailableData(dataLine) } returns newSamples

        advanceTimeBy(1)
        scope.coroutineContext.cancelChildren()

        coVerify { subscriber1.received(newSamples) }
        coVerify { subscriber2.received(newSamples) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `empty samples are not given to the subscribers`() = scope.runTest {
        createSUT()
        every { targetDataLineReader.getAvailableData(dataLine) } returns nextByteArray(0)

        advanceTimeBy(1)
        scope.coroutineContext.cancelChildren()

        coVerify(exactly = 0) { subscriber1.received(any()) }
        coVerify(exactly = 0) { subscriber2.received(any()) }
    }

    @Test
    fun `get the audio format`() {
        val sut = createSUT()
        every { dataLine.format } returns format
        assertEquals(dataLine.format, sut.audioFormat)
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is`() {
        val sut = createSUT()

        val actual = sut.checkIfSubscribed(subscriber1)

        Assertions.assertTrue(actual)
    }

    @Test
    fun `check if a potential subscriber is subscribed when it is not`() {
        val sut = createSUT()

        val newSubscriber: LineListenerSubscriber = mockk()
        val actual = sut.checkIfSubscribed(newSubscriber)

        assertFalse(actual)
    }

    @Test
    fun `add a subscriber`() {
        val sut = createSUT()

        val newSubscriber: LineListenerSubscriber = mockk()
        sut.addSubscriber(newSubscriber)

        Assertions.assertTrue(sut.checkIfSubscribed(newSubscriber))
    }

}
