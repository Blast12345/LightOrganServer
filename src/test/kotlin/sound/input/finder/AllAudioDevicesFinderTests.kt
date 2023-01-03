package sound.input.finder

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextMixer
import toolkit.monkeyTest.nextMixerInfo
import javax.sound.sampled.AudioSystem

class AllAudioDevicesFinderTests {

    private val mixerInfo = nextMixerInfo()
    private val mixer = nextMixer()
    private val mixers = listOf(mixer)

    @BeforeEach
    fun setup() {
        mockkStatic(AudioSystem::class)
        every { AudioSystem.getMixerInfo() } returns arrayOf(mixerInfo)
        every { AudioSystem.getMixer(mixerInfo) } returns mixer
    }

    private fun createSUT(): AllAudioDevicesFinder {
        return AllAudioDevicesFinder()
    }

    @Test
    fun `find all audio devices`() {
        val sut = createSUT()
        val actual = sut.getAudioDevices()
        assertEquals(mixers, actual)
    }

}