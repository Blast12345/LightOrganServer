package audio.audioDevice

import audio.audioDevice.AudioDeviceFactory
import audio.audioInput.AudioInput
import audio.audioInput.AudioInputFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextString
import javax.sound.sampled.Line
import javax.sound.sampled.Mixer
import javax.sound.sampled.Port
import javax.sound.sampled.TargetDataLine

class AudioDeviceFactoryTests {

    private val audioInputFactory: AudioInputFactory = mockk()

    private val mixer: Mixer = mockk()

    // NOTE: Allegedly, audio can only be captured from TargetDataLines
    private val portInfo: Port.Info = mockk()
    private val input1Info: Line.Info = mockk()
    private val input2Info: Line.Info = mockk()

    private val inputLine1: TargetDataLine = mockk()
    private val inputLine2: TargetDataLine = mockk()

    private val audioInput1: AudioInput = mockk()
    private val audioInput2: AudioInput = mockk()
    private val allAudioInputs = setOf(audioInput1, audioInput2)

    @BeforeEach
    fun setupHappyPath() {
        every { audioInputFactory.create(any(), any()) } returns mockk()

        every { mixer.mixerInfo.name } returns nextString("name")
        every { mixer.targetLineInfo } returns arrayOf(portInfo, input1Info, input2Info)

        every { portInfo.lineClass } returns Port::class.java
        every { input1Info.lineClass } returns TargetDataLine::class.java
        every { input2Info.lineClass } returns TargetDataLine::class.java

        every { mixer.getLine(input1Info) } returns inputLine1
        every { mixer.getLine(input2Info) } returns inputLine2
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): AudioDeviceFactory {
        return AudioDeviceFactory(audioInputFactory)
    }

    // Name
    @Test
    fun `given the mixer has a name, then name is the mixer name`() {
        val sut = createSUT()

        val audioDevice = sut.create(mixer)

        assertEquals(mixer.mixerInfo.name, audioDevice.name)
    }

    @Test
    fun `given the mixer has no name, then name is unknown`() {
        val sut = createSUT()
        every { mixer.mixerInfo.name } returns null

        val audioDevice = sut.create(mixer)

        assertEquals("Unknown", audioDevice.name)
    }

    // Inputs
    @Test
    fun `given the mixer has inputs, then the device has those inputs`() {
        val sut = createSUT()
        val mixerName = mixer.mixerInfo.name
        every { audioInputFactory.create(mixerName, inputLine1) } returns audioInput1
        every { audioInputFactory.create(mixerName, inputLine2) } returns audioInput2

        val audioDevice = sut.create(mixer)

        assertEquals(allAudioInputs, audioDevice.inputs.toSet())
    }

    @Test
    fun `given the mixer has no name, then the device has those inputs with a generic name`() {
        val sut = createSUT()
        every { mixer.mixerInfo.name } returns null
        every { audioInputFactory.create("Unknown", inputLine1) } returns audioInput1
        every { audioInputFactory.create("Unknown", inputLine2) } returns audioInput2

        val audioDevice = sut.create(mixer)

        assertEquals(allAudioInputs, audioDevice.inputs.toSet())
    }

    @Test
    fun `given the mixers does not have inputs, then the device has no inputs`() {
        val sut = createSUT()
        every { mixer.targetLineInfo } returns arrayOf(portInfo)

        val audioDevice = sut.create(mixer)

        assertTrue(audioDevice.inputs.isEmpty())
    }

}