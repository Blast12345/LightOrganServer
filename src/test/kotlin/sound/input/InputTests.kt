package sound.input

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.TargetDataLine

class InputTests {

    @MockK
    private lateinit var dataLine: TargetDataLine

    @MockK
    private lateinit var rawWaveFactory: RawWaveFactory

    private val bufferSize = 4096
    private val bytesAvailable = 1024
    private val bytesRead = 1024
    private val format = AudioFormat(44100F, 8, 1, true, true)
    private val rawWave = doubleArrayOf(1.0)

    @Before
    fun setup() {
        dataLine = mockk<TargetDataLine>()
        every { dataLine.bufferSize } returns bufferSize
        every { dataLine.open() } returns Unit
        every { dataLine.start() } returns Unit
        every { dataLine.available() } returns bytesAvailable
        every { dataLine.read(any(), 0, bytesAvailable) } returns bytesRead
        every { dataLine.format } returns format

        rawWaveFactory = mockk<RawWaveFactory>()
        every { rawWaveFactory.rawWaveFrom(any(), format) } returns rawWave
    }

    private fun createSUT(): Input {
        return Input(dataLine, rawWaveFactory)
    }

    @Test
    fun `return the audio sample when the data line has new data`() {
        val sut = createSUT()

        var nextSample: DoubleArray? = null
        sut.listenForAudioSamples {
            nextSample = it

            // We must stop listening or the while loop will prevent us from continuing
            sut.stopListening()
        }

        assertEquals(rawWave, nextSample)
    }

    @Test
    fun `the line is opened and started before looking for data availability`() {
        val sut = createSUT()

        sut.listenForAudioSamples {
            sut.stopListening()
        }

        verifyOrder {
            dataLine.open()
            dataLine.start()
            dataLine.available()
        }
    }


}