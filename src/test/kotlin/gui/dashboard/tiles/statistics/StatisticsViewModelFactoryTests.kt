package gui.dashboard.tiles.statistics

import gui.dashboard.tiles.Statistics.FrequencyResolutionCalculator
import gui.dashboard.tiles.Statistics.LowestDiscernibleFrequencyCalculator
import gui.dashboard.tiles.Statistics.SecondsOfAudioUsedCalculator
import gui.dashboard.tiles.Statistics.StatisticsViewModelFactory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFormat
import toolkit.monkeyTest.nextConfig

class StatisticsViewModelFactoryTests {

    private val secondsOfAudioUsedCalculator: SecondsOfAudioUsedCalculator = mockk()
    private val lowestDiscernibleFrequencyCalculator: LowestDiscernibleFrequencyCalculator = mockk()
    private val frequencyResolutionCalculator: FrequencyResolutionCalculator = mockk()

    private val audioFormat = nextAudioFormat()
    private val config = nextConfig()

    private val secondsOfAudioUsed = 1.111f
    private val lowestDiscernibleFrequency = 2.222f
    private val frequencyResolution = 3.333f

    @BeforeEach
    fun setup() {
        every { secondsOfAudioUsedCalculator.calculate(any(), any(), any()) } returns secondsOfAudioUsed
        every { lowestDiscernibleFrequencyCalculator.calculate(any()) } returns lowestDiscernibleFrequency
        every { frequencyResolutionCalculator.calculate(any(), any(), any()) } returns frequencyResolution
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): StatisticsViewModelFactory {
        return StatisticsViewModelFactory(
            secondsOfAudioUsedCalculator = secondsOfAudioUsedCalculator,
            lowestDiscernibleFrequencyCalculator = lowestDiscernibleFrequencyCalculator,
            frequencyResolutionCalculator = frequencyResolutionCalculator
        )
    }

    @Test
    fun `the duration of audio used contains the calculated value and the unit of measure`() {
        val sut = createSUT()

        val viewModel = sut.create(audioFormat, config)

        assertEquals("1.11 seconds", viewModel.durationOfAudioUsed)
    }

    @Test
    fun `the duration of audio used calculation takes the relevant data-points`() {
        val sut = createSUT()

        sut.create(audioFormat, config)

        verify {
            secondsOfAudioUsedCalculator.calculate(
                sampleSize = config.sampleSize,
                sampleRate = audioFormat.sampleRate,
                numberOfChannels = audioFormat.channels
            )
        }
    }

    @Test
    fun `the lowest discernible frequency contains the calculated value and the unit of measure`() {
        val sut = createSUT()

        val viewModel = sut.create(audioFormat, config)

        assertEquals("2.22 Hz", viewModel.lowestDiscernibleFrequency)
    }

    @Test
    fun `the lowest discernible frequency calculation takes the relevant data-points`() {
        val sut = createSUT()

        sut.create(audioFormat, config)

        verify { lowestDiscernibleFrequencyCalculator.calculate(secondsOfAudioUsed) }
    }

    @Test
    fun `the frequency resolution contains the calculated value and the unit of measure`() {
        val sut = createSUT()

        val viewModel = sut.create(audioFormat, config)

        assertEquals("3.33 Hz", viewModel.frequencyResolution)
    }

    @Test
    fun `the frequency resolution calculation takes the relevant data-points`() {
        val sut = createSUT()

        sut.create(audioFormat, config)

        verify {
            frequencyResolutionCalculator.calculate(
                sampleRate = audioFormat.sampleRate,
                sampleSize = config.sampleSize,
                numberOfChannels = audioFormat.channels
            )
        }
    }

}