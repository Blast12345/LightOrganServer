package lightOrgan.spectrum

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import dsp.bins.frequency.FrequencyBins

data class SpectrumManagerFixture(
    val mock: SpectrumManager,
    val frequencyBins: MutableStateFlow<FrequencyBins>
) {

    companion object {
        fun create(): SpectrumManagerFixture {
            val fixture = SpectrumManagerFixture(
                mock = mockk<SpectrumManager>(),
                frequencyBins = MutableStateFlow(emptyList())
            )

            every { fixture.mock.frequencyBins } returns fixture.frequencyBins

            return fixture
        }
    }

}