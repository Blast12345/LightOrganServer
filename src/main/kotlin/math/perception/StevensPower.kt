package math.perception

// Reference: https://en.wikipedia.org/wiki/Stevens%27s_power_law
enum class StevensPower(val exponent: Double) {
    LOUDNESS_3KHZ_TONE(0.67),
    BRIGHTNESS_5DEG_IN_DARK(0.33),
    BRIGHTNESS_POINT_SOURCE(0.5),
    BRIGHTNESS_BRIEF_FLASH(0.5),
    BRIGHTNESS_POINT_SOURCE_BRIEF_FLASH(1.0),
}