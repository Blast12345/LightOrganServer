package colorBroadcaster

import config.Config
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import server.ServerInterface
import toolkit.monkeyTest.nextColor
import kotlin.random.Random

class ColorBroadcasterTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = TestScope()
    private val delegate: ColorBroadcasterDelegate = mockk()
    private val config: Config = mockk()
    private val server: ServerInterface = mockk()

    private val nextColor = nextColor()
    private val millisecondsToWait = Random.nextLong(1024)

    @BeforeEach
    fun setup() {
        coEvery { delegate.getNextColor() } returns nextColor
        coEvery { config.millisecondsToWaitBetweenCheckingForNextColor } returns millisecondsToWait
        coEvery { server.sendColor(any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createSUT(): ColorBroadcaster {
        return ColorBroadcaster(
            scope = scope,
            delegate = delegate,
            config = config,
            server = server
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `check for the next color every X milliseconds`() = scope.runTest {
        createSUT()
        val loops = Random.nextInt(10)

        advanceTimeBy(loops * millisecondsToWait)
        coVerify(exactly = loops) { delegate.getNextColor() }

        scope.coroutineContext.cancelChildren()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `send the next color to the server when the next color is available`() = scope.runTest {
        createSUT()

        advanceTimeBy(millisecondsToWait)
        coVerify(exactly = 1) { server.sendColor(nextColor) }

        scope.coroutineContext.cancelChildren()
    }

}