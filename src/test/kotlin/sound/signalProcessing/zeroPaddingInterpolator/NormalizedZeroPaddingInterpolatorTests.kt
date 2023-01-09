package sound.signalProcessing.zeroPaddingInterpolator

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDoubleArray
import kotlin.random.Random

class NormalizedZeroPaddingInterpolatorTests {

    private var interpolator: ZeroPaddingInterpolatorInterface = mockk()
    private val interpolatorNormalizer: ZeroPaddingInterpolatorNormalizerInterface = mockk()

    private val signal = nextDoubleArray()
    private val desiredSize = Random.nextInt()

    private val interpolatorOutput = nextDoubleArray()
    private val normalizerOutput = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { interpolator.interpolate(any(), any()) } returns interpolatorOutput
        every { interpolatorNormalizer.normalize(any(), any()) } returns normalizerOutput
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): NormalizedZeroPaddingInterpolator {
        return NormalizedZeroPaddingInterpolator(
            interpolator = interpolator,
            interpolatorNormalizer = interpolatorNormalizer
        )
    }

    @Test
    fun `return a normalized interpolated signal`() {
        val sut = createSUT()
        val interpolatedSignal = sut.interpolate(signal, desiredSize)
        Assertions.assertArrayEquals(normalizerOutput, interpolatedSignal, 0.001)
        verify { interpolator.interpolate(signal, desiredSize) }
        verify { interpolatorNormalizer.normalize(interpolatorOutput, signal.size) }
    }

}