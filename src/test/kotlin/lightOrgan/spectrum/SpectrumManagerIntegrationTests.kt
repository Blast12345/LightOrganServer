package lightOrgan.spectrum

import kotlinx.coroutines.ExperimentalCoroutinesApi

// The processing chain is so long and specific that unit tests seemed like a mirror of implementation
// rather than checks for meaningful behavior. As such, integration tests seemed like the right tool.
@OptIn(ExperimentalCoroutinesApi::class)
class SpectrumManagerIntegrationTests {

//    private val sampleRate = 48000f
//    private val config: SpectrumConfig = mockk()
//
//    private val frequency = 60f
//    private val tone = generateSineWave(frequency, sampleRate, amplitude = 1f)
//    private val silence = generateSineWave(frequency, sampleRate, amplitude = 0f)
//
//    private val toneFrame = nextAudioFrame(tone)
//    private val silenceFrame = nextAudioFrame(silence)
//    private val interleavedFrame = nextAudioFrame(tone, silence)
//
//    private val collectionScope = TestScope()
//
//    @BeforeEach
//    fun setupHappyPath() {
//        every { config.sampleSize } returns (sampleRate / 10).toInt()
//        every { config.interpolatedSampleSize } returns sampleRate.toInt()
//        every { config.highPassFilter } returns null
//        every { config.lowPassFilter } returns null
//    }
//
//    @AfterEach
//    fun tearDown() {
//        collectionScope.cancel()
//    }
//
//
//    private fun createSUT(): SpectrumManager {
//        return SpectrumManager(config)
//    }
//
//    // Frequency Bins
//    @Test
//    fun `given a tone, the peak bin is at the frequency of the wave`() {
//        val sut = createSUT()
//
//        val bins = sut.calculate(toneFrame)
//
//        val peakBin = bins.maxBy { it.magnitude }
//        assertEquals(frequency, peakBin.frequency, 0.1f)
//    }
//
//    @Test
//    fun `window correction produces expected magnitude for full volume sine wave`() {
//        val sut = createSUT()
//
//        val bins = sut.calculate(toneFrame)
//
//        val peakBin = bins.maxBy { it.magnitude }
//        assertEquals(1f, peakBin.magnitude, 0.1f)
//    }
//
//    @Test
//    fun `silence produces near-zero magnitudes`() {
//        val sut = createSUT()
//
//        val bins = sut.calculate(silenceFrame)
//
//        bins.forEach { assertTrue(it.magnitude < 0.1f) }
//    }
//
//    // TODO: Or unit test instead?
//    @Test
//    fun `frequencies that are too long for the sample size are excluded`() {
//        val lowestFrequency = 100f // 100 for easy math
//        val cycleDuration = 1f / lowestFrequency // 100 Hz takes 0.01 seconds to complete a cycle
//        val samples = (sampleRate * cycleDuration).toInt()
//        every { config.sampleSize } returns samples
//        val sut = createSUT()
//
//        val bins = sut.calculate(toneFrame)
//
//        bins.forEach { assertTrue(it.frequency >= lowestFrequency) }
//    }
//
//    // Multichannel
//    @Test
//    fun `stereo input is mixed to mono before processing`() {
//        val sut = createSUT()
//
//        val bins = sut.calculate(interleavedFrame)
//
//        // Half-amplitude because only one channel (i.e., half) has the tone
//        val peakBin = bins.maxBy { it.magnitude }
//        assertEquals(60f, peakBin.frequency, 1f)
//        assertEquals(0.5f, peakBin.magnitude, 0.1f)
//    }
//
//    // DSP Filtering
//    @Test
//    fun `given a frequency is below the high pass cutoff, it is filtered`() {
//        val sut = createSUT()
//        every { config.highPassFilter } returns FilterConfig.highPassFromSlope(
//            FilterFamily.BUTTERWORTH,
//            frequency * 2f,
//            6
//        )
//
//        val bins = sut.calculate(toneFrame)
//
//        val peakBin = bins.maxBy { it.magnitude }
//        assertEquals(frequency, peakBin.frequency, 0.1f)
//        assertTrue(peakBin.magnitude <= 0.5f)
//    }
//
//    @Test
//    fun `given a frequency is above the low pass cutoff, it is filtered`() {
//        val sut = createSUT()
//        every { config.lowPassFilter } returns FilterConfig.lowPassFromSlope(
//            FilterFamily.BUTTERWORTH,
//            frequency / 2f,
//            6
//        )
//
//        val bins = sut.calculate(toneFrame)
//
//        val peakBin = bins.maxBy { it.magnitude }
//        assertEquals(frequency, peakBin.frequency, 0.1f)
//        assertTrue(peakBin.magnitude <= 0.5f)
//    }
//
//    // Emission
//    @Test
//    fun `the frequency bins are emitted after calculation`() {
//        val sut = createSUT()
//        val received = sut.frequencyBins.collectInto(collectionScope)
//
//        val bins = sut.calculate(toneFrame)
//        collectionScope.advanceUntilIdle()
//
//        assertEquals(listOf(bins), received)
//    }

}