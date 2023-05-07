package input.lineListener

import config.Config
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextByteArray
import toolkit.monkeyTest.nextPositiveLong
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine
import kotlin.random.Random

class LineListenerTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = TestScope()

    private val config: Config = mockk()
    private val subscriber: LineListenerSubscriber = mockk()
    private val dataLine: TargetDataLine = mockk()
    private val targetDataLineReader: TargetDataLineReader = mockk()
    private val checkInterval = nextPositiveLong()

    private val newSamples = nextByteArray()
    private val format: AudioFormat = mockk()

    @BeforeEach
    fun setup() {
        every { config.millisecondsToWaitBetweenCheckingForNewAudio } returns checkInterval
        every { subscriber.received(any()) } returns Unit
        every { dataLine.open() } returns Unit
        every { dataLine.start() } returns Unit
        every { targetDataLineReader.getAvailableData(any()) } returns newSamples
        every { dataLine.format } returns format
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createSUT(): LineListener {
        return LineListener(
            config = config,
            subscribers = mutableSetOf(subscriber),
            dataLine = dataLine,
            targetDataLineReader = targetDataLineReader,
            scope = scope
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

        val iterations = Random.nextInt(1, 8)
        val durationOfTwoLoops = iterations * checkInterval
        advanceTimeBy(durationOfTwoLoops)

        coVerify(exactly = iterations) { targetDataLineReader.getAvailableData(dataLine) }

        scope.coroutineContext.cancelChildren()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `samples are given to the subscribers`() = scope.runTest {
        createSUT()

        advanceTimeBy(1)

        coVerify { subscriber.received(newSamples) }
        scope.coroutineContext.cancelChildren()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `empty samples are not given to the subscribers`() = scope.runTest {
        createSUT()
        every { targetDataLineReader.getAvailableData(any()) } returns nextByteArray(0)

        advanceTimeBy(1)

        coVerify(exactly = 0) { subscriber.received(any()) }
        scope.coroutineContext.cancelChildren()
    }

    @Test
    fun `get the audio format`() {
        val sut = createSUT()
        assertEquals(dataLine.format, sut.audioFormat)
    }

}