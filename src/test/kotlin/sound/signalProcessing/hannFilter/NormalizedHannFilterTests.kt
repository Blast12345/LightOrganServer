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

    private var hannFilter: HannFilterInterface = mockk()
    private val hannFilterNormalizer: HannFilterNormalizerInterface = mockk()

    private val signal = nextDoubleArray()

    private val filterOutput = nextDoubleArray()
    private val normalizerOutput = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { hannFilter.filter(any()) } returns filterOutput
        every { hannFilterNormalizer.normalize(any()) } returns normalizerOutput
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): NormalizedHannFilter {
        return NormalizedHannFilter(
            hannFilter = hannFilter,
            hannFilterNormalizer = hannFilterNormalizer
        )
    }

    @Test
    fun `return a normalized filter signal`() {
        val sut = createSUT()
        val filteredSignal = sut.filter(signal)
        assertArrayEquals(normalizerOutput, filteredSignal, 0.001)
        verify { hannFilter.filter(signal) }
        verify { hannFilterNormalizer.normalize(filterOutput) }
    }

}