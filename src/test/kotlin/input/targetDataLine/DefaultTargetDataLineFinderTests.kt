package input.targetDataLine

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextTargetDataLineList

class DefaultTargetDataLineFinderTests {

    private var targetDataLinesFinder: TargetDataLinesFinder = mockk()
    private val targetDataLines = nextTargetDataLineList(2)

    @BeforeEach
    fun setup() {
        every { targetDataLinesFinder.find() } returns targetDataLines
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DefaultTargetDataLineFinder {
        return DefaultTargetDataLineFinder(targetDataLinesFinder)
    }

    @Test
    fun `return the first data line`() {
        val sut = createSUT()
        val actual = sut.find()
        assertEquals(targetDataLines.first(), actual)
    }

}