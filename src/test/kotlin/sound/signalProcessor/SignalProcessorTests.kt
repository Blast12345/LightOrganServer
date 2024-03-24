package sound.signalProcessor

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import sound.signalProcessor.filters.window.WindowFilter
import sound.signalProcessor.interpolator.ZeroPaddingInterpolator
import toolkit.monkeyTest.nextDoubleArray

class SignalProcessorTests {

    private val samples = nextDoubleArray()
    private val windowFilter: WindowFilter = mockk()
    private val windowedSamples = nextDoubleArray()
    private val sampleInterpolator: ZeroPaddingInterpolator = mockk()
    private val interpolatedSamples = nextDoubleArray()

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): SignalProcessor {
        return SignalProcessor(
            windowFilter = windowFilter,
            sampleInterpolator = sampleInterpolator
        )
    }

    @Test
    fun `create usable samples from an audio frame`() {
        val sut = createSUT()
        every { windowFilter.applyTo(samples) } returns windowedSamples
        every { sampleInterpolator.interpolate(windowedSamples) } returns interpolatedSamples

        val actual = sut.process(samples)

        assertArrayEquals(interpolatedSamples, actual)
    }

}
