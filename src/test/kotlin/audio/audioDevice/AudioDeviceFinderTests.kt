package audio.audioDevice

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Mixer

class AudioDeviceFinderTests {

    private val audioDeviceFactory: AudioDeviceFactory = mockk()

    private val mixerInfo1: Mixer.Info = mockk()
    private val mixer1: Mixer = mockk()
    private val audioDevice1: AudioDevice = mockk()

    private val mixerInfo2: Mixer.Info = mockk()
    private val mixer2: Mixer = mockk()
    private val audioDevice2: AudioDevice = mockk()

    @BeforeEach
    fun setupHappyPath() {
        mockkStatic(AudioSystem::class)

        every { AudioSystem.getMixerInfo() } returns arrayOf(mixerInfo1, mixerInfo2)

        every { AudioSystem.getMixer(mixerInfo1) } returns mixer1
        every { AudioSystem.getMixer(mixerInfo2) } returns mixer2

        every { audioDeviceFactory.create(mixer1) } returns audioDevice1
        every { audioDeviceFactory.create(mixer2) } returns audioDevice2
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): AudioDeviceFinder {
        return AudioDeviceFinder(audioDeviceFactory)
    }

    @Test
    fun `find all audio devices`() {
        val sut = createSUT()

        val actual = sut.findAll()

        val audioDevices = setOf(audioDevice1, audioDevice2)
        assert(actual.toSet() == audioDevices)
    }

}
