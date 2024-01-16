package input.targetDataLine

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextMixer
import toolkit.monkeyTest.nextMixerInfo
import javax.sound.sampled.AudioSystem

class AudioDevicesFinderTests {

    private val mixerInfo = nextMixerInfo()
    private val mixer = nextMixer()
    private val mixers = listOf(mixer)

    @BeforeEach
    fun setup() {
        mockkStatic(AudioSystem::class)
        every { AudioSystem.getMixerInfo() } returns arrayOf(mixerInfo)
        every { AudioSystem.getMixer(mixerInfo) } returns mixer
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): AudioDevicesFinder {
        return AudioDevicesFinder()
    }

    @Test
    fun `find all audio devices`() {
        val sut = createSUT()
        val actual = sut.find()
        assertEquals(mixers, actual)
    }

}
