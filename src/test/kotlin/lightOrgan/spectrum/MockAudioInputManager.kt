package lightOrgan.spectrum

import bins.FrequencyBins
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

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