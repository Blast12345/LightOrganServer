package math.geometry

@JvmInline
value class Angle private constructor(
    val radians: Double
) {

    val degrees: Double get() = Math.toDegrees(radians)
    val turns: Double get() = radians / (2 * Math.PI)
    val normalized: Angle get() = fromTurns(turns.mod(1.0)) // TODO: Test me

    companion object {
        fun fromRadians(value: Double) = Angle(value)
        fun fromDegrees(value: Double) = Angle(Math.toRadians(value))
        fun fromTurns(value: Double) = Angle(value * 2 * Math.PI)
    }

}