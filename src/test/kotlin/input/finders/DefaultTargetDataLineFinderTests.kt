package input.finders

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextTargetDataLineList

class DefaultTargetDataLineFinderTests {

    private var allInputsFinder: AllInputsFinder = mockk()
    private val targetDataLines = nextTargetDataLineList(2)

    @BeforeEach
    fun setup() {
        every { allInputsFinder.getInputs() } returns targetDataLines
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): DefaultTargetDataLineFinder {
        return DefaultTargetDataLineFinder(allInputsFinder)
    }

    @Test
    fun `return the first input`() {
        val sut = createSUT()
        val actual = sut.find()
        assertEquals(targetDataLines.first(), actual)
    }

}