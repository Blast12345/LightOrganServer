package math.physics

typealias RadiantFlux = Double

// TODO: Test me
// This is a modeling of how light works in the real world (i.e. not based on human perception).
// It is measured in terms of radiant flux, of which the SI unit is watts.
// Light mixing is inherently linear. 500 watts + 500 watts = 1000 watts
// And like light in the real world, the brightness is not capped, so it can be any arbitrarily large size.
class Light(
    val red: RadiantFlux = 0.0,
    val green: RadiantFlux = 0.0,
    val blue: RadiantFlux = 0.0
) {

    val radiantFlux: RadiantFlux by lazy {
        red + green + blue
    }

    operator fun plus(other: Light) = Light(
        red = red + other.red,
        green = green + other.green,
        blue = blue + other.blue
    )

    operator fun times(factor: Double) = Light(
        red = red * factor,
        green = green * factor,
        blue = blue * factor
    )

}