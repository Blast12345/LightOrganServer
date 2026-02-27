package lightOrgan.color

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import wrappers.color.Color

data class ColorManagerFixture(
    val mock: ColorManager,
    val color: MutableStateFlow<Color>
) {

    companion object {
        fun create(): ColorManagerFixture {
            val fixture = ColorManagerFixture(
                mock = mockk<ColorManager>(),
                color = MutableStateFlow(Color.black)
            )

            every { fixture.mock.color } returns fixture.color

            return fixture
        }
    }

}