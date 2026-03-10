package math.geometry

@JvmInline
value class Angle private constructor(
    val radians: Double
) {

    val degrees: Double get() = Math.toDegrees(radians)
    val turns: Double get() = radians / (2 * Math.PI)

    companion object {
        fun fromRadians(value: Double) = Angle(value)
        fun fromRadians(value: Float) = Angle(value.toDouble())
        fun fromDegrees(value: Double) = Angle(Math.toRadians(value))
        fun fromTurns(value: Double) = Angle(value * 2 * Math.PI)
    }

}