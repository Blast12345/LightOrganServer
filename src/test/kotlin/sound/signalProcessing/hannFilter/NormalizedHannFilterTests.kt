package sound.signalProcessing.hannFilter

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray

class NormalizedHannFilterTests {

    private var standardHannFilter: StandardHannFilter = mockk()
    private val hannFilterNormalizer: HannFilterNormalizer = mockk()

    private val signal = nextDoubleArray()

    private val filterOutput = nextDoubleArray()
    private val normalizerOutput = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { standardHannFilter.filter(any()) } returns filterOutput
        every { hannFilterNormalizer.normalize(any()) } returns normalizerOutput
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): NormalizedHannFilter {
        return NormalizedHannFilter(
            standardHannFilter = standardHannFilter,
            hannFilterNormalizer = hannFilterNormalizer
        )
    }

    @Test
    fun `return a normalized filter signal`() {
        val sut = createSUT()
        val filteredSignal = sut.filter(signal)
        assertArrayEquals(normalizerOutput, filteredSignal, 0.001)
        verify { standardHannFilter.filter(signal) }
        verify { hannFilterNormalizer.normalize(filterOutput) }
    }

}