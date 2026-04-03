package config

import config.children.Client
import dsp.filtering.FilterConfig
import dsp.filtering.FilterFamily
import dsp.filtering.FilterOrder
import dsp.filtering.FilterType
import dsp.windowing.WindowType
import gui.dashboard.tiles.spectrum.SpectrumGuiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import lightOrgan.spectrum.SpectrumConfig
import music.WesternTuningSystem
import kotlin.time.Duration.Companion.milliseconds

class ConfigFactory(
    private val persistedConfig: PersistedConfig = PersistedConfig()
) {

    fun create(): Config {
        val tuning = WesternTuningSystem()

        return Config(
            startAutomatically = MutableStateFlow(persistedConfig.startAutomatically),
            clients = setOf(Client("192.168.1.55")),
            spectrum = SpectrumConfig(
                frameDuration = 63.milliseconds,
                approximateBinSpacing = 1f,
                rolloffThreshold = -48f,
                highPassFilter = FilterConfig(
                    type = FilterType.HighPass(tuning.getFrequency(tuning.A, octave = 0)),
                    family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(48)),
                ),
                lowPassFilter = FilterConfig(
                    type = FilterType.LowPass(tuning.getFrequency(tuning.A, octave = 2)),
                    family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(48)),
                ),
                window = WindowType.BlackmanHarris3Term
            ),
            spectrumGui = SpectrumGuiConfig(
                scale = 4F,
                lowestFrequency = 0f,
                highestFrequency = 160F,
            ),
            brightnessMultiplier = 3F,
        )
    }

}
