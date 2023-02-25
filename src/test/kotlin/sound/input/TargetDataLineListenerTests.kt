package sound.input

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
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine
import kotlin.random.Random

class TargetDataLineListenerTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = TestScope()
    private var dataLine: TargetDataLine = mockk()
    private var delegate: TargetDataLineListenerDelegate = mockk()
    private val config: Config = mockk()

    private val bytesAvailable = 1024
    private val newSamples = Random.nextBytes(bytesAvailable)
    private val format: AudioFormat = mockk()
    private val millisecondsBetweenChecks = Random.nextLong(100)

    @BeforeEach
    fun setup() {
        every { dataLine.open() } returns Unit
        every { dataLine.start() } returns Unit
        every { dataLine.available() } returns bytesAvailable
        every { dataLine.format } returns format

        // Target Data Lines return data through the first parameter, so I must do this nasty mocking.
        every { dataLine.read(any(), any(), any()) } answers {
            val outputParameter = invocation.args[0] as ByteArray
            newSamples.copyInto(outputParameter)
            bytesAvailable
        }

        every { delegate.received(any(), any()) } returns Unit
        every { config.millisecondsToWaitBetweenCheckingForNewAudio } returns millisecondsBetweenChecks
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createSUT(): TargetDataLineListener {
        return TargetDataLineListener(
            scope = scope,
            dataLine = dataLine,
            delegate = delegate,
            config = config
        )
    }

    @Test
    fun `prepare the data line to be read from during initialization`() {
        createSUT()
        verifyOrder {
            dataLine.open()
            dataLine.start()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `give the delegate new samples and associated format if they are available`() = scope.runTest {
        createSUT()
        advanceTimeBy(1)
        coVerify { delegate.received(newSamples, format) }
        scope.coroutineContext.cancelChildren()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `check for new samples on the data line every X milliseconds`() = scope.runTest {
        createSUT()

        val durationOfTwoLoops = 2 * millisecondsBetweenChecks
        advanceTimeBy(durationOfTwoLoops)

        coVerify(exactly = 2) { dataLine.available() }

        scope.coroutineContext.cancelChildren()
    }

    @Test
    fun `get the delegate`() {
        val sut = createSUT()
        val actual = sut.getDelegate()
        assertEquals(delegate, actual)
    }

    @Test
    fun `set the delegate`() {
        val sut = createSUT()
        val newDelegate: TargetDataLineListenerDelegate = mockk()
        sut.setDelegate(newDelegate)
        assertEquals(sut.getDelegate(), newDelegate)
    }

}