package lightOrgan.sound.signalProcessing

import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lightOrgan.sound.signalProcessing.hannFilter.HannFilterInterface
import lightOrgan.sound.signalProcessing.zeroPaddingInterpolator.ZeroPaddingInterpolatorInterface
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextDoubleArray
import kotlin.random.Random

class SignalProcessorTests {

    private var config: Config = mockk()
    private val sampleExtractor: SampleExtractorInterface = mockk()
    private var hannFilter: HannFilterInterface = mockk()
    private val interpolator: ZeroPaddingInterpolatorInterface = mockk()

    private val audioFrame = nextAudioFrame()

    private val sampleSize: Int = Random.nextInt()
    private val extractedSamples = nextDoubleArray()
    private val filteredSamples = nextDoubleArray()
    private val interpolatedSampleSize: Int = Random.nextInt()
    private val interpolatedSamples = nextDoubleArray()

    @BeforeEach
    fun setup() {
        every { config.sampleSize } returns sampleSize
        every { sampleExtractor.extract(any(), any()) } returns extractedSamples
        every { hannFilter.filter(any()) } returns filteredSamples
        every { config.interpolatedSampleSize } returns interpolatedSampleSize
        every { interpolator.interpolate(any(), any()) } returns interpolatedSamples
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): SignalProcessor {
        return SignalProcessor(
            config = config,
            sampleExtractor = sampleExtractor,
            hannFilter = hannFilter,
            interpolator = interpolator
        )
    }

    @Test
    fun `the sample size is reduced to increase responsiveness`() {
        val sut = createSUT()
        sut.process(audioFrame)
        verify { sampleExtractor.extract(audioFrame, sampleSize) }
    }

    @Test
    fun `the extracted samples go through a window filter to prevent smearing`() {
        val sut = createSUT()
        sut.process(audioFrame)
        verify { hannFilter.filter(extractedSamples) }
    }

    @Test
    fun `the filtered samples are interpolated to increase frequency resolution`() {
        val sut = createSUT()
        sut.process(audioFrame)
        verify { interpolator.interpolate(filteredSamples, interpolatedSampleSize) }
    }

    @Test
    fun `return the interpolated samples`() {
        val sut = createSUT()
        val samples = sut.process(audioFrame)
        assertArrayEquals(interpolatedSamples, samples, 0.001)
    }

}