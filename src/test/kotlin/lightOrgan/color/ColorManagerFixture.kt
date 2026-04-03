package lightOrgan.color

import androidx.compose.ui.graphics.Color
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

data class ColorManagerFixture(
    val mock: ColorManager,
    val colorFlow: MutableStateFlow<Color>
) {

    companion object {
        fun create(): ColorManagerFixture {
            val fixture = ColorManagerFixture(
                mock = mockk<ColorManager>(),
                colorFlow = MutableStateFlow(Color.Black)
            )

            every { fixture.mock.color } returns fixture.colorFlow

            return fixture
        }
    }

}