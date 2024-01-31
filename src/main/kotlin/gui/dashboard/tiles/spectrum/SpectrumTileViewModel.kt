import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import sound.bins.frequency.FrequencyBins

class SpectrumTileViewModel {
    val frequencyBins: MutableState<FrequencyBins> = mutableStateOf(listOf())

    fun updateFrequencyBins(newFrequencyBins: FrequencyBins) {
        MainScope().launch {
            frequencyBins.value = newFrequencyBins
        }
    }
}
