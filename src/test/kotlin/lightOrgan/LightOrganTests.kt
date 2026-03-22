package lightOrgan

import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class LightOrganTests {

//    private lateinit var audioInputManager: AudioInputManagerFixture
//    private lateinit var spectrumManager: SpectrumManagerFixture
//    private lateinit var colorManager: ColorManagerFixture
//    private val audioBuffer: AccumulatingAudioBuffer = mockk()
//    private val server: Server = mockk()
//    private val sutScope = TestScope()
//
//    private val frequencyBins = nextFrequencyBins()
//
//    private val newAudio = nextAudioFrame()
//    private val bufferedAudio = nextAudioFrame()
//    private val newColor = nextColor()
//
//    @BeforeEach
//    fun setupHappyPath() {
//        audioInputManager = AudioInputManagerFixture.create()
//        spectrumManager = SpectrumManagerFixture.create()
//        colorManager = ColorManagerFixture.create()
//
//        every { audioBuffer.append(newAudio) } returns Unit
//        coEvery { audioBuffer.drain() } returns bufferedAudio coAndThen { awaitCancellation() }
//        every { spectrumManager.mock.calculate(bufferedAudio) } returns frequencyBins
//        every { colorManager.mock.calculate(frequencyBins) } returns newColor
//        every { server.new(newColor) } returns Unit
//    }
//
//    @AfterEach
//    fun tearDown() {
//        sutScope.cancel()
//        clearAllMocks()
//    }
//
//    private fun createSUT(): LightOrgan {
//        return LightOrgan(
//            audioInputManager = audioInputManager.mock,
//            spectrumManager = spectrumManager.mock,
//            colorManager = colorManager.mock,
//            server = server,
//            audioBuffer = audioBuffer,
//            scope = sutScope
//        )
//    }
//
//    @Test
//    fun `when new audio is received, broadcast the color`() = runTest {
//        val sut = createSUT()
//        sutScope.advanceUntilIdle()
//
//        audioInputManager.audioStream.emit(newAudio)
//        sutScope.advanceUntilIdle()
//
//        verify { server.new(newColor) }
//    }

}
