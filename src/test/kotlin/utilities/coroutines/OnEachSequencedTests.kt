package utilities.coroutines

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextString

class OnEachSequencedTests {

    private val gapDetector: SequenceGapDetector = mockk()

    private val string1 = nextString("1")
    private val string2 = nextString("2")

    @BeforeEach
    fun setup() {
        every { gapDetector.check(any()) } returns 0L
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `perform the action with the inner value`() = runTest {
        val received = mutableListOf<String>()

        flowOf(string1.asSequenced(0), string2.asSequenced(1))
            .onEachSequenced { received.add(it) }
            .toList()

        assertEquals(
            listOf(string1, string2),
            received
        )
    }

    @Test
    fun `report when gaps occurs`() = runTest {
        val gaps = mutableListOf<Long>()
        every { gapDetector.check(0) } returns 0L
        every { gapDetector.check(2) } returns 1L

        flowOf(string1.asSequenced(0), string2.asSequenced(2))
            .onEachSequenced(gapDetector = gapDetector, action = {}, onGap = { gaps.add(it) })
            .toList()

        assertEquals(
            listOf(1L),
            gaps
        )
    }

}