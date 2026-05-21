package lightOrgan.color

import color.RgbColor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

data class ColorManagerFixture(
    val mock: ColorManager,
    val colorFlow: MutableStateFlow<RgbColor>
) {

    companion object {
        fun create(): ColorManagerFixture {
            val fixture = ColorManagerFixture(
                mock = mockk<ColorManager>(),
                colorFlow = MutableStateFlow(RgbColor.Black)
            )

            every { fixture.mock.color } returns fixture.colorFlow

            return fixture
        }
    }

}