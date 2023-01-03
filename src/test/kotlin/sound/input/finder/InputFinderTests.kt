package sound.input.finder

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextTargetDataLineList

class InputFinderTests {

    private lateinit var allInputsFinder: AllInputsFinderInterface
    private val targetDataLines = nextTargetDataLineList()

    @BeforeEach
    fun setup() {
        allInputsFinder = mockk()
        every { allInputsFinder.getInputs() } returns targetDataLines
    }

    private fun createSUT(): InputFinder {
        return InputFinder(allInputsFinder)
    }

    @Test
    fun `return the first input`() {
        val sut = createSUT()
        val actual = sut.getInput()
        assertEquals(targetDataLines.first(), actual)
    }

}