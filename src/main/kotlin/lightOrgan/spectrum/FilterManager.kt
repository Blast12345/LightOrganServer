package lightOrgan.spectrum

import audio.samples.AudioFrame
import bins.FrequencyBin
import bins.FrequencyBins
import dsp.filtering.Filter
import dsp.filtering.config.FilterBuilder
import dsp.filtering.config.FilterConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log10

// TODO: Magnitude typealias with toDBFS() and vice versa dbfs.toMagnitude()
// ENHANCEMENT: Show the filter response in the UI
// ENHANCEMENT: Make configs configurable via the UI, then automatically rebuild filters
class FilterManager(
    initialConfigs: List<FilterConfig>,
    private val filterBuilder: FilterBuilder = FilterBuilder(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
) {

    private val _configs = MutableStateFlow(initialConfigs)
    private val _response = MutableStateFlow<FrequencyBins>(emptyList())
    private var usableFrequencies: HashSet<Float> = hashSetOf()
    private var filters: List<Filter> = emptyList()

    init {
        scope.launch {
            _configs.collect {
                filters = emptyList()
                _response.value = emptyList()
                usableFrequencies = hashSetOf()
            }
        }
    }

    fun filter(audio: AudioFrame): AudioFrame {
        rebuildFiltersIfNeeded(audio.format.sampleRate)

        val filtered = filters.fold(audio.samples) { samples, filter ->
            filter.filter(samples)
        }

        return AudioFrame(filtered, audio.format)
    }

    fun responseFor(frequencies: List<Float>, thresholdDb: Float): FrequencyBins {
        if (_response.value.size == frequencies.size && _response.value.isNotEmpty()) return _response.value
        if (filters.isEmpty()) return emptyList()

        return frequencies.map { frequency ->
            FrequencyBin(
                frequency = frequency,
                magnitude = filters.fold(1f) { mag, filter -> mag * filter.magnitudeAt(frequency) }
            )
        }.also {
            _response.value = it
            usableFrequencies = it
                .filter { bin -> 20f * log10(bin.magnitude) >= thresholdDb }
                .mapTo(hashSetOf()) { bin -> bin.frequency }
        }
    }

    fun usableFrequencies(): Set<Float> = usableFrequencies

    private fun rebuildFiltersIfNeeded(sampleRate: Float) {
        if (filters.isNotEmpty() && filters.all { it.sampleRate == sampleRate }) return

        filters = _configs.value.map { filterBuilder.build(it, sampleRate) }
    }

}