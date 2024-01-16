package sound.signalProcessor.filters.window

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray

class HannFilterTests {

    private val samples = nextDoubleArray()
    private var algorithm: HannFilterAlgorithm = mockk()
    private val algorithmSamples = nextDoubleArray()
    private val corrector: HannFilterCorrector = mockk()
    private val correctedSamples = nextDoubleArray()

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): HannFilter {
        return HannFilter(
            algorithm = algorithm,
            corrector = corrector
        )
    }

    @Test
    fun `filter samples using a hann filter with correction`() {
        val sut = createSUT()
        every { algorithm.applyTo(samples) } returns algorithmSamples
        every { corrector.correct(algorithmSamples) } returns correctedSamples

        val actual = sut.applyTo(samples)

        assertArrayEquals(correctedSamples, actual, 0.001)
    }

}
