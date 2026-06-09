package utilities.coroutines

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextInt

class MapSequencedTests {

    private val gapDetector: SequenceGapDetector = mockk()

    private val number1 = nextInt()
    private val number2 = nextInt()

    @BeforeEach
    fun setup() {
        every { gapDetector.check(any()) } returns 0L
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `transform the inner value`() = runTest {
        val result = flowOf(number1.asSequenced(0L))
            .mapSequenced { it * 2 }
            .first()

        assertEquals(number1 * 2, result.value)
    }

    @Test
    fun `outgoing sequence is independent of incoming`() = runTest {
        val results = flowOf(number1.asSequenced(10L), number2.asSequenced(11L))
            .mapSequenced { it }
            .toList()

        assertEquals(
            listOf(Sequenced(0L, number1), Sequenced(1L, number2)),
            results
        )
    }

    @Test
    fun `report when gaps occur`() = runTest {
        val gaps = mutableListOf<Long>()
        every { gapDetector.check(0) } returns 0L
        every { gapDetector.check(2) } returns 1L

        flowOf(number1.asSequenced(0), number2.asSequenced(2))
            .mapSequenced(gapDetector = gapDetector, transform = { it }, onGap = { gaps.add(it) })
            .toList()

        assertEquals(
            listOf(1L),
            gaps
        )
    }

}