package toolkit.monkeyTest

import java.awt.Color
import kotlin.random.Random

fun nextColor(): Color {
    return Color.getHSBColor(
        Random.nextFloat(),
        Random.nextFloat(),
        Random.nextFloat()
    )
}
