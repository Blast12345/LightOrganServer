package sound.input.finder

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextTargetLineInfo
import javax.sound.sampled.Mixer
import javax.sound.sampled.TargetDataLine

class AllInputsFinderTests {

    private lateinit var input1: TargetDataLine
    private lateinit var inputs: List<TargetDataLine>
    private lateinit var mixer1: Mixer
    private lateinit var mixer2: Mixer
    private val targetLineInfo = nextTargetLineInfo()
    private lateinit var audioDevices: List<Mixer>
    private lateinit var allAudioDevicesFinder: AllAudioDevicesFinderInterface

    @BeforeEach
    fun setup() {
        input1 = mockk()
        inputs = listOf(input1)

        mixer1 = mockk()
        every { mixer1.targetLineInfo } returns arrayOf(targetLineInfo)
        every { mixer1.getLine(targetLineInfo) } returns input1

        mixer2 = mockk()
        every { mixer2.targetLineInfo } returns arrayOf()
        every { mixer2.getLine(targetLineInfo) } returns null

        audioDevices = listOf(mixer1, mixer2)

        allAudioDevicesFinder = mockk()
        every { allAudioDevicesFinder.getAudioDevices() } returns audioDevices
    }

    private fun createSUT(): AllInputsFinder {
        return AllInputsFinder(allAudioDevicesFinder)
    }

    @Test
    fun `find all inputs`() {
        val sut = createSUT()
        val actual = sut.getInputs()
        assertEquals(inputs, actual)
    }

}

