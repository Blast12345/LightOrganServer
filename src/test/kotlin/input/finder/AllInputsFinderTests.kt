package input.finder

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextTargetLineInfo
import javax.sound.sampled.Mixer
import javax.sound.sampled.TargetDataLine

class AllInputsFinderTests {

    private var input1: TargetDataLine = mockk()
    private var allAudioDevicesFinder: AllAudioDevicesFinder = mockk()
    private val inputs = listOf(input1)
    private var mixer1: Mixer = mockk()
    private var mixer2: Mixer = mockk()
    private val targetLineInfo = nextTargetLineInfo()
    private val audioDevices = listOf(mixer1, mixer2)

    @BeforeEach
    fun setup() {
        every { mixer1.targetLineInfo } returns arrayOf(targetLineInfo)
        every { mixer1.getLine(targetLineInfo) } returns input1
        every { mixer2.targetLineInfo } returns arrayOf()
        every { mixer2.getLine(targetLineInfo) } returns null
        every { allAudioDevicesFinder.getAudioDevices() } returns audioDevices
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
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

