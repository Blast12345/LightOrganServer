//package lightOrgan
//
//import io.mockk.*
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runCurrent
//import kotlinx.coroutines.test.runTest
//import lightOrgan.color.ColorManagerFixture
//import lightOrgan.input.AudioInputManagerFixture
//import lightOrgan.spectrum.SpectrumManagerFixture
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import server.Server
//import toolkit.monkeyTest.nextAudioStreamFrame
//import toolkit.monkeyTest.nextColor
//import toolkit.monkeyTest.nextFrequencyBins
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class LightOrganTests {
//
//    private lateinit var inputManager: AudioInputManagerFixture
//    private lateinit var spectrumManager: SpectrumManagerFixture
//    private lateinit var colorManager: ColorManagerFixture
//    private val server: Server = mockk()
//
//    private val streamFrame = nextAudioStreamFrame()
//    private val frequencyBins = nextFrequencyBins()
//    private val newColor = nextColor()
//
//    @BeforeEach
//    fun setupHappyPath() {
//        inputManager = AudioInputManagerFixture.create()
//        spectrumManager = SpectrumManagerFixture.create()
//        colorManager = ColorManagerFixture.create()
//
//        coEvery { spectrumManager.mock.calculate(streamFrame.audio) } returns frequencyBins
//        coEvery { colorManager.mock.calculate(frequencyBins) } returns newColor
//        every { server.new(newColor) } returns Unit
//    }
//
//    @AfterEach
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    private fun createSUT(): LightOrgan {
//        return LightOrgan(
//            inputManager = inputManager.mock,
//            spectrumManager = spectrumManager.mock,
//            colorManager = colorManager.mock,
//            server = server
//        )
//    }
//
//    @Test
//    fun `when an audio frame is received, then a color is derived and broadcast`() = runTest {
//        val sut = createSUT()
//        sut.start(backgroundScope)
//        runCurrent()
//
//        inputManager.audioStream.emit(streamFrame)
//        runCurrent()
//
//        verify { server.new(newColor) }
//    }
//
//}
