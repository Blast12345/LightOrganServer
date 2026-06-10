package extensions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextException
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineScopeTryLaunchTests {

    @Test
    fun `execute the block`() = runTest {
        var executed = false

        backgroundScope.tryLaunch(
            block = { executed = true },
            onError = { }
        )

        runCurrent()

        assertTrue(executed)
    }

    @Test
    fun `when the block throws, handle the exception through an error block`() = runTest {
        val exception = nextException()
        var received: Exception? = null

        backgroundScope.tryLaunch(
            block = { throw exception },
            onError = { received = it }
        )
        runCurrent()

        assertEquals(exception, received)
    }

    @Test
    fun `do not consume cancellation exceptions`() = runTest {
        var onErrorCalled = false

        val job = backgroundScope.tryLaunch(
            block = { throw CancellationException("test") },
            onError = { onErrorCalled = true }
        )
        runCurrent()

        assertTrue(job.isCancelled)
        assertFalse(onErrorCalled)
    }
}